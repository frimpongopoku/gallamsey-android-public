package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SetLocationsPage extends AppCompatActivity {

/**
 * THIS IS ONE OF FIRST THINGS THAT WERE CREATED, so its behind on all the cool things "locationProtocol" can do,
 * Will change later
 * */
  LocationProtocol locationsProtocol;
  FusedLocationProviderClient locationProviderClient;
  Location deviceLocation;
  LocationListener locationListener;
  LocationManager locationManager;
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  CollectionReference communities = db.collection(Konstants.COMMUNITIES_COLLECTION);
  //--Widgets---------------
  Button saveButton, startButton;
  ProgressBar spinner;
  TextView textNotification;
  Spinner regionsDropdown;
  //------------------------
  Boolean servicesOK, gpsOK, permissionsOK;
  int btnCheck = 0;
  public ArrayList<String> regionsArrayList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location);
    locationsProtocol = new LocationProtocol(this);
    spinner = findViewById(R.id.spinner);
    textNotification = findViewById(R.id.text_notification);
    saveButton = findViewById(R.id.save_my_location);
    startButton = findViewById(R.id.start_button);
    startLocationListener();
    regionsDropdown = findViewById(R.id.regions_dropdown);
    regionsArrayList.add("Choose A Region");
    getCommunitiesListFromFirebase("GHANA", getCommunitiesCallback);
    ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, regionsArrayList);
    regionsDropdown.setAdapter(dropdownAdapter);
    regionsDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
  }


  private CommunitiesCallback getCommunitiesCallback = new CommunitiesCallback() {
    @Override
    public void collectCommunities(ArrayList<HashMap<String, Object>> communities) {
      for (int i = 0; i < communities.size(); i++) {
        try {
          regionsArrayList.add(communities.get(i).get("name").toString());
        } catch (Exception e) {
          Log.d("gettingCommunities", e.getMessage());
        }
      }
    }
  };

  public void getCommunitiesListFromFirebase(String country, final CommunitiesCallback callback) {
    communities.document(country)
      .get()
      .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
          if (documentSnapshot.exists()) {
            ArrayList<HashMap<String, Object>> communities = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("Regions");
            callback.collectCommunities(communities);
          }
        }
      }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Toast.makeText(SetLocationsPage.this, e.getMessage(), Toast.LENGTH_LONG).show();
      }
    });

  }

  public void saveUserLocation(View v) {
    //----disable location listener 
    locationManager.removeUpdates(locationListener);
    locationManager = null;
    Toast.makeText(this, "Dude, you are tryna save some shit here!", Toast.LENGTH_SHORT).show();
  }

  public void startLocationListener() {
    locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

      return;
    }
    locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    locationListener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        deviceLocation = location;
        spinner.setVisibility(View.INVISIBLE);
        if (btnCheck == 1) {
          textNotification.setText("Thanks for waiting, we have your location now.");
          startButton.setVisibility(View.GONE);
          saveButton.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onStatusChanged(String s, int i, Bundle bundle) {

      }

      @Override
      public void onProviderEnabled(String s) {

      }

      @Override
      public void onProviderDisabled(String s) {

      }
    };
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
  }

  /**
   * 1. First check for google play services
   * -- if its not fine, show a dialog that tells the user how to get google play services
   * 2. Check if user has location services?
   * -- if they don't, then they cant run this particular fxnality
   * 3. Check if user has location services turned on
   * -- if its not turned on, show the user to their settings, so they can turn it on
   */

  public void getUserLocation(View v) {
    getDeviceLocation("Sorry, for some reason we could not calculate your current location");
  }

  private void getDeviceLocation(final String errorMsg) {
    spinner.setVisibility(View.VISIBLE);
    textNotification.setVisibility(View.VISIBLE);
    btnCheck = 1;
    if (checkSelfPermission(locationsProtocol.FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(locationsProtocol.COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      //This is never going to happen in real life, because our check is always in "onResume"
      // so it will be solved before the user even gets here
      //However, this check is required by google
      return;
    }
    locationProviderClient.getLastLocation()
      .addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
        @Override
        public void onComplete(@NonNull Task<android.location.Location> task) {
          if (task.isSuccessful()) {
            Location location = task.getResult();
            if (location != null) {
              String[] coords = {String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())};
              Log.w("hereAreYourCoords::->", location.getLatitude() + " ," + location.getLongitude());
              spinner.setVisibility(View.GONE);
              textNotification.setText("Thanks for waiting, we have your location now.");
              startButton.setVisibility(View.GONE);
              saveButton.setVisibility(View.VISIBLE);
            } else {
              Log.w("onLocationNull::->", "Your location is null bro!");
            }
          } else {
            if (!errorMsg.isEmpty()) {
              Toast.makeText(SetLocationsPage.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
          }

        }
      }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.w("couldntGetLocation::", e.getMessage());
        if (!errorMsg.isEmpty()) {
          Toast.makeText(SetLocationsPage.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
      }
    });

  }


  private boolean checkAllLocationRequirements() {
    if (locationsProtocol.isServiceOk()) {
      Log.w("ServicesOK::--->", "Google service is okay!");
      servicesOK = true;
      if (locationsProtocol.isGPSEnabled()) {
        Log.w("GPS ENABLED::--->", "GPS is enabled!");
        gpsOK = true;
        if (locationsProtocol.isPermissionGranted()) {
          permissionsOK = true;
//          Toast.makeText(this, "Nice, you have all the requirements to calculate your current location!", Toast.LENGTH_SHORT).show();
          return true;
        } else {
          locationsProtocol.askForPermissions(this, Konstants.PERMISSION_REQUEST_CODE);
        }
      } else {
        locationsProtocol.turnGPSOn();
      }
    } else {
      locationsProtocol.resolveService(this);
    }
    return false;
  }

  @Override
  protected void onResume() {
    super.onResume();
    checkAllLocationRequirements();
    startLocationListener();
  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == Konstants.PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        permissionsOK = true;
      }
    }
  }


  public void goHome(View v) {
    Intent home = new Intent(this, Home.class);
    startActivity(home);
    finish();
  }

  public interface CommunitiesCallback {
    void collectCommunities(ArrayList<HashMap<String, Object>> communities);
  }

}


