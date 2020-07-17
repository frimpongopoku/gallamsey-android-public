package com.pongo.zembe;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class GallamseyLocationComponent implements Parcelable {
  private String locationName, longitude, latitude, endPointLong, endPointLat, endPointLowerLat, endPointLowerLong;
  private float radius;
  private float[] radiusResult = new float[1];
  private Location endPoint;
  private Location endPointLowerTier;
  private Location location;


  public GallamseyLocationComponent() {
  } //for firebase

  public GallamseyLocationComponent(String locationName, double longitude, double latitude) {
    this.locationName = locationName;
    this.longitude = String.valueOf(longitude);
    this.latitude = String.valueOf(latitude);
    this.location = new Location(Konstants.INIT_STRING);
    this.location.setLatitude(latitude);
    this.location.setLongitude(longitude);
    this.endPoint = LocationProtocol.generatePointOfSensibleDistanceAway(this.location,Konstants.UPPER_TIER);
    this.endPointLowerTier = LocationProtocol.generatePointOfSensibleDistanceAway(this.location,Konstants.LOWER_TIER);
    this.endPointLat = String.valueOf(this.endPoint.getLatitude());
    this.endPointLong = String.valueOf(this.endPoint.getLongitude());
    this.endPointLowerLat = String.valueOf(this.endPointLowerTier.getLatitude());
    this.endPointLowerLong = String.valueOf(this.endPointLowerTier.getLongitude());
    Location.distanceBetween(latitude,longitude,this.endPoint.getLatitude(),this.endPoint.getLongitude(),this.radiusResult);
    this.radius = this.radiusResult[0];
  }

  public String getEndPointLowerLat() {
    return endPointLowerLat;
  }

  public String getEndPointLowerLong() {
    return endPointLowerLong;
  }

  public Location getEndPointLowerTier() {
    return endPointLowerTier;
  }

  public String getEndPointLong() {
    return endPointLong;
  }

  public void setEndPointLong(String endPointLong) {
    this.endPointLong = endPointLong;
  }


  public Location getLocation() {
    return location;
  }

  public String getEndPointLat() {
    return endPointLat;
  }

  public void setEndPointLat(String endPointLat) {
    this.endPointLat = endPointLat;
  }

  public float getRadius() {
    return radius;
  }

  public void setRadius(float radius) {
    this.radius = radius;
  }

  public Location getEndPoint() {
    return endPoint;
  }

  public void setEndPoint(Location endPoint) {
    this.endPoint = endPoint;
  }

  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.locationName);
    dest.writeString(this.longitude);
    dest.writeString(this.latitude);
    dest.writeString(this.endPointLong);
    dest.writeString(this.endPointLat);
    dest.writeValue(this.radius);
  }

  protected GallamseyLocationComponent(Parcel in) {
    this.locationName = in.readString();
    this.longitude = in.readString();
    this.latitude = in.readString();
    this.endPointLong = in.readString();
    this.endPointLat = in.readString();
    this.radius = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  public static final Creator<GallamseyLocationComponent> CREATOR = new Creator<GallamseyLocationComponent>() {
    @Override
    public GallamseyLocationComponent createFromParcel(Parcel source) {
      return new GallamseyLocationComponent(source);
    }

    @Override
    public GallamseyLocationComponent[] newArray(int size) {
      return new GallamseyLocationComponent[size];
    }
  };
}
