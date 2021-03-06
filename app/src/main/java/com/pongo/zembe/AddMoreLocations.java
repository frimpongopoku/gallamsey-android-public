package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddMoreLocations extends AppCompatActivity implements LocationItemClicked {
  ProgressBar spinner;
  RecyclerView recyclerView;
  LocationRecyclerAdapter adapter;
  ArrayList<GallamseyLocationComponent> arr = new ArrayList<>();
  ArrayList<GallamseyLocationComponent> onLoadArr = new ArrayList<>();
  MagicBoxes magicBoxes;
  Button start;
  EditText nameBox;
  LocationProtocol locationsProtocol;
  FusedLocationProviderClient locationProviderClient;
  LocationListener locationListener;
  LocationManager locationManager;
  Location deviceLocation = null;
  GroundUser authenticatedUser;
  FirebaseFirestore firestore = FirebaseFirestore.getInstance();
  CollectionReference userDB = firestore.collection(Konstants.USER_COLLECTION);
  GalFirebaseHelper firebaseHelper = new GalFirebaseHelper();
  Context context = this;
  TextView pageName;
  ImageView backBtn;
  String howIGotHere;
  Context thisActivity ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_more_locations);
    thisActivity = this;
    howIGotHere = getIntent().getStringExtra(Konstants.COMING_FROM_PROFILE_EDIT);
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    if (authenticatedUser != null) {
      arr = authenticatedUser.getDeliveryLocations();
      onLoadArr = authenticatedUser.getDeliveryLocations();
      listenForAuthUserChanges();
    }
    initializeActivity();

  }


  private void initializeActivity() {
    backBtn = findViewById(R.id.back_btn);
    backBtn.setOnClickListener(goBack);
    Log.d("authUserHere", authenticatedUser.toString());
    start = findViewById(R.id.location_start_button);
    start.setOnClickListener(userLocationClickListener);
    nameBox = findViewById(R.id.location_name);
    magicBoxes = new MagicBoxes(this);
    recyclerView = findViewById(R.id.location_list_recycler);
    adapter = new LocationRecyclerAdapter(this, arr, this);
    adapter.setAuthenticatedUser(authenticatedUser);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    new ItemTouchHelper(locationSwipeFunctionality).attachToRecyclerView(recyclerView);
    locationsProtocol = new LocationProtocol(this);
    spinner = findViewById(R.id.spinner);
    pageName = findViewById(R.id.page_name);
    pageName.setText("Manage Your Locations");

  }

  private View.OnClickListener goBack = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      if(howIGotHere == null) finish();
      // this means user came from profile editing after registration....
      // So homepage wont be opened  already, so open with the back button
      Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
      Intent page = new Intent(thisActivity, Home.class);
      page.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
      startActivity(page);
      finish();
      view.setEnabled(false);
    }
  };

  private void listenForAuthUserChanges() {
    DocumentReference userRef = userDB.document(authenticatedUser.getUserDocumentID());
    firebaseHelper.setSnapshotListenerOnDocument(userRef, new GalInterfaceGuru.SnapshotTakerInterface() {
      @Override
      public void callback(DocumentSnapshot document) {
        authenticatedUser = document.toObject(GroundUser.class);
        //RESET RECYCLER VIEW TO REFLECT CHANGES
        recyclerView.setAdapter(null);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        adapter = new LocationRecyclerAdapter(getApplicationContext(), authenticatedUser.getDeliveryLocations(), (LocationItemClicked) context);
        adapter.setAuthenticatedUser(authenticatedUser);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
      }
    });
  }

  private ItemTouchHelper.SimpleCallback locationSwipeFunctionality = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
      return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
      final int itemPosition = viewHolder.getAdapterPosition();
      GallamseyLocationComponent location = arr.get(itemPosition);
      magicBoxes.constructASimpleDialog("Confirm Location Deletion", "Are you sure you want to delete '" + location.getLocationName() + "'?", new MagicBoxCallables() {
        @Override
        public void negativeBtnCallable() {
          // do nothing
        }

        @Override
        public void positiveBtnCallable() {
          arr.remove(itemPosition);
          adapter.notifyItemRemoved(itemPosition);
          authenticatedUser.setDeliveryLocations(arr);
          userDB.document(authenticatedUser.getUserDocumentID())
            .set(authenticatedUser).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Log.d("couldNotRemoveLocation", e.getMessage());
            }
          });
        }
      }).show();

    }
  };
  private View.OnClickListener persistLocation = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      userDB.document(authenticatedUser.getUserDocumentID())
        .set(authenticatedUser)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            start.setEnabled(false);
            start.setAlpha((float) 0.30);
            Toast.makeText(AddMoreLocations.this, "Your location has now been saved", Toast.LENGTH_SHORT).show();
          }
        }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Toast.makeText(AddMoreLocations.this, "For some reason awe could not save your current location.. sorry", Toast.LENGTH_SHORT).show();
          Log.d("couldntSaveLocation", e.getMessage());
        }
      });
    }
  };

  private void switchButtons(String mode) {
    switch (mode) {
      case "START": {
        Toast.makeText(this, "Chilling ouchere", Toast.LENGTH_SHORT).show();
        start.setOnClickListener(userLocationClickListener);
        start.setBackgroundResource(R.drawable.login_button_shape);
        start.setText("START");
      }
      case "SAVE": {
        nameBox.setVisibility(View.GONE);
        start.setOnClickListener(persistLocation);
        start.setBackgroundResource(R.drawable.green_button_rounded_solid);
        start.setText("Save Location");
      }
    }
  }

  private View.OnClickListener userLocationClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      String name = nameBox.getText().toString().trim();
      GallamseyLocationComponent newLocation;
      if (name.isEmpty()) {
        nameBox.setError("First add the name of your location, then start");
        nameBox.requestFocus();
      } else {
        // if location is already determined before the user clicks start, "deviceLocation" will not be null anymore
        //lets get it!
        if (deviceLocation != null) {
          newLocation = new GallamseyLocationComponent(name, deviceLocation.getLongitude(), deviceLocation.getLatitude());
          arr.add(newLocation);
          adapter.notifyDataSetChanged();
          nameBox.setText("");
          switchButtons("SAVE");
          Toast.makeText(AddMoreLocations.this, "Nice, Location Recorded", Toast.LENGTH_SHORT).show();
        } else {
          tryToGetLocation(name);
        }
      }
    }
  };

  @Override
  protected void onResume() {
    super.onResume();
    listenForAuthUserChanges();
    locationsProtocol.checkAllRequirements();
    locationsProtocol.startLocationListener(new LocationProtocol.LocationRetriever() {
      @Override
      public void callback(Location location, LocationManager manager, LocationListener listener) {
        deviceLocation = location;
        locationManager = manager;
        locationListener = listener;
      }
    });
  }

  public void tryToGetLocation(final String name) {
    spinner.setVisibility(View.VISIBLE);
    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
      @Override
      public void onSuccess(Location location) {
        if (location != null) {
          deviceLocation = location;
          GallamseyLocationComponent loc = new GallamseyLocationComponent(name, location.getLongitude(), location.getLatitude());
          arr.add(loc);
          adapter.notifyDataSetChanged();
          nameBox.setText("");
          switchButtons("SAVE");
          Toast.makeText(AddMoreLocations.this, "Nice, location recorded", Toast.LENGTH_SHORT).show();
        } else {
          spinner.setVisibility(View.GONE);
          Toast.makeText(AddMoreLocations.this, "For some reason, we could not get your location", Toast.LENGTH_SHORT).show();
        }
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.d("gettingLocation", e.getMessage());
      }
    });

  }

  private void setPrimaryLocation(final GallamseyLocationComponent location) {
    authenticatedUser.setGeoLocation(location);
    userDB.document(authenticatedUser.getUserDocumentID()).set(authenticatedUser).addOnSuccessListener(new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void aVoid) {
        Toast.makeText(AddMoreLocations.this, location.getLocationName() + " has been made your primary location", Toast.LENGTH_SHORT).show();
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        e.printStackTrace();
        Toast.makeText(AddMoreLocations.this, e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }


  @Override
  public void locationItemCallback(int position) {
    final GallamseyLocationComponent location = arr.get(position);
    String text = "You only see errands that are within a particular radius of your primary location. Would you like to set '" + location.getLocationName() + "' as your primary location?";
    magicBoxes.constructASimpleDialog("Set Primary Location", text, new MagicBoxCallables() {
      @Override
      public void negativeBtnCallable() {

      }

      @Override
      public void positiveBtnCallable() {
        setPrimaryLocation(location);
      }
    }).show();
  }
}
