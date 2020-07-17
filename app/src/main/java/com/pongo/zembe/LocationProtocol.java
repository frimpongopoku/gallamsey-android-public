package com.pongo.zembe;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationProtocol {
  private final Context appContext;
  private int SERVICE_CHECK_RESULT = 0;
  private int SERVICE_INSTALLATION_REQ_CODE = 2;
  public String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
  public String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
  private String[] perimissions_array = {FINE_LOCATION, COARSE_LOCATION};
  FusedLocationProviderClient locationProviderClient;
  LocationListener locationListener;
  LocationManager locationManager;


  public LocationProtocol(Context context) {
    this.appContext = context;
  }


  public static Location generatePointOfSensibleDistanceAway(Location location) {
    //Just add the magic number "0.001" to the coordinate's lat & long values
    double lat, longi, refinedLat, refinedLong;
    lat = location.getLatitude();
    longi = location.getLongitude();
    refinedLat = switchToPositiveOrNegative(Konstants.POSITIVE,lat);
    refinedLong = switchToPositiveOrNegative(Konstants.POSITIVE,longi);
    //now add
    refinedLat = refinedLat + Konstants.MAGIC_DECIMAL;
    refinedLong = refinedLong + Konstants.MAGIC_DECIMAL;
    //now return coords with to their original signs
    Location loc = new Location(Konstants.INIT_STRING);
    if(lat < 0.0){
      loc.setLatitude(switchToPositiveOrNegative(Konstants.NEGATIVE,refinedLat));
    }
    else{
      loc.setLatitude(refinedLat);
    }

    if(longi <0.0){
      loc.setLongitude(switchToPositiveOrNegative(Konstants.NEGATIVE,refinedLong));
    }
    else{
      loc.setLongitude(refinedLong);
    }
    return loc;
  }

  public static double switchToPositiveOrNegative(String type, double val) {
    if (type.equals(Konstants.NEGATIVE)) { //change my value to negative
      if (val < 0.0) {//if its already -ve, leave it
        return val;
      }
      return -1.0 * val; // if its not, multiply by -1
    } else if (type.equals(Konstants.POSITIVE)) {//change my value to +ve
      if (val < 0.0) { // if value is a -ve number, multiply by -1 and bring me +ve
        return -1 * val;
      }
      return val; // it you get here, you are already +ve, GET OUT
    }
    return 0.0;
  }

  public boolean isServiceOk() {
    int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(appContext);
    SERVICE_CHECK_RESULT = available;
    if (available == ConnectionResult.SUCCESS) {
      return true;
    }
    return false;
  }

  public boolean resolveService(Activity activity) {
    if (GoogleApiAvailability.getInstance().isUserResolvableError(SERVICE_CHECK_RESULT)) {
      Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, SERVICE_CHECK_RESULT, SERVICE_INSTALLATION_REQ_CODE);
      dialog.show();
      return true;
    }
    // If service gets here, it means that phone cannot run google maps.
    return false;
  }

  public boolean isGPSEnabled() {
    LocationManager locationManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      return false;
    }
    return true;
  }

  public void turnGPSOn() {
    AlertDialog.Builder dialog = new AlertDialog.Builder(appContext);
    dialog.setMessage("This application requires you to turn on your location services. Would you like to turn it on?")
      .setCancelable(false)
      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          Intent enableGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          appContext.startActivity(enableGPS);
        }
      });
    AlertDialog alert = dialog.create();
    alert.show();
  }

  public boolean isPermissionGranted() {
    // Check if permission to access location is already given
    if (ContextCompat.checkSelfPermission(appContext, FINE_LOCATION)
      == PackageManager.PERMISSION_GRANTED &&
      ContextCompat.checkSelfPermission(appContext, FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      return true;
    }
    return false;
  }

  public void askForPermissions(Activity activity, int requestCode) {
    //request user to give permission to his/her location
    ActivityCompat.requestPermissions(activity, perimissions_array, requestCode);
  }

  public Boolean checkAllRequirements() {
    if (isServiceOk()) {
      Log.d("ServicesOK::--->", "Google service is okay!");

      if (isGPSEnabled()) {
        Log.d("GPS ENABLED::--->", "GPS is enabled!");
        if (isPermissionGranted()) {
          return true;
        } else {
          askForPermissions((Activity) appContext, Konstants.PERMISSION_REQUEST_CODE);
        }
      } else {
        turnGPSOn();
      }
    } else {
      resolveService((Activity) appContext);
    }
    return false;
  }

  public void startLocationListener(final LocationRetriever locationRetriever) {
    locationManager = (LocationManager) appContext.getSystemService(appContext.LOCATION_SERVICE);
    if (appContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && appContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    locationProviderClient = LocationServices.getFusedLocationProviderClient(appContext);
    locationListener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        locationRetriever.callback(location, locationManager, locationListener);

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
    //use request updates from both Network & GPS ( one of them must hit ) lol!
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);

  }

  public interface LocationRetriever {
    void callback(Location location, LocationManager locationManager, LocationListener listener);
  }
}

