package com.pongo.zembe;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class LocationProtocol {
  private final Context appContext;
  private int SERVICE_CHECK_RESULT = 0;
  private int SERVICE_INSTALLATION_REQ_CODE = 2;
  public String  FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
  public String  COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
  private String[] perimissions_array = {FINE_LOCATION,COARSE_LOCATION};


  public LocationProtocol(Context context){
    this.appContext = context;
  }

  public boolean isServiceOk() {
    int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(appContext);
    SERVICE_CHECK_RESULT = available;
    if(available == ConnectionResult.SUCCESS){
      return true;
    }
    return false;
  }

  public boolean resolveService(Activity activity){
    if(GoogleApiAvailability.getInstance().isUserResolvableError(SERVICE_CHECK_RESULT)){
      Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity,SERVICE_CHECK_RESULT,SERVICE_INSTALLATION_REQ_CODE);
      dialog.show();
      return true;
    }
    // If service gets here, it means that phone cannot run google maps.
    return false;
  }

  public boolean isGPSEnabled(){
    LocationManager locationManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
    if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
      return false;
    }
    return true;
  }
  public void turnGPSOn(){
    AlertDialog.Builder dialog = new AlertDialog.Builder(appContext);
    dialog.setMessage("This application requires you to turn on your location services. Would you like to turn it on?")
      .setCancelable(false)
      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick( DialogInterface dialogInterface, int i) {
          Intent enableGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          appContext.startActivity(enableGPS);
        }
      });
    AlertDialog alert = dialog.create();
    alert.show();
  }

  public boolean isPermissionGranted(){
    // Check if permission to access location is already given
    if(ContextCompat.checkSelfPermission(appContext,FINE_LOCATION)
      == PackageManager.PERMISSION_GRANTED &&
      ContextCompat.checkSelfPermission(appContext,FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED){
      return true;
    }
    return false;
  }

  public void askForPermissions(Activity activity,int requestCode){
    //request user to give permission to his/her location
    ActivityCompat.requestPermissions(activity,perimissions_array,requestCode);
  }



}
