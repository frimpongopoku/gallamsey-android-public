package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class SetLocationsPage extends AppCompatActivity {

  LocationProtocol locationsProtocol;
  Boolean servicesOK, gpsOK, permissionsOK;
  FusedLocationProviderClient locationProviderClient;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location);
    locationsProtocol = new LocationProtocol(this);
    locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

  }

  /**
   * 1. First check for google play services
   * -- if its not fine, show a dialog that tells the user how to get google play services
   * 2. Check if user has location services?
   * -- if they dont, then they cant run this particular fxnality
   * 3. Check if user has location services turned on
   * -- if its not turned on, show the user to their settings, so they can turn it on
   */
  public void getUserLocation(View v) {
    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
              Location location= task.getResult();
              String[] coords = {String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())};
            Toast.makeText(SetLocationsPage.this, location.getLatitude()+" ,"+location.getLongitude(), Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(SetLocationsPage.this, "Calculating your location failed!", Toast.LENGTH_SHORT).show();
          }

        }
      }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.w("couldntGetLocation::", e.getMessage());
        Toast.makeText(SetLocationsPage.this, "Sorry, for some reason, we could not caluclate your current location!", Toast.LENGTH_SHORT).show();
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
          Toast.makeText(this, "Nice, you have all the requirements to calculate your current location!", Toast.LENGTH_SHORT).show();
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
  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == Konstants.PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        permissionsOK = true;
      }
    }
  }

}
