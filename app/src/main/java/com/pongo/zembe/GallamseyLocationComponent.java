package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class GallamseyLocationComponent implements Parcelable {
  private String locationName, longitude, latitude;

  public GallamseyLocationComponent() {
  } //for firebase

  public GallamseyLocationComponent(String locationName, double longitude, double latitude) {
    this.locationName = locationName;
    this.longitude = String.valueOf(longitude);
    this.latitude = String.valueOf(latitude);
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
  }

  protected GallamseyLocationComponent(Parcel in) {
    this.locationName = in.readString();
    this.longitude = in.readString();
    this.latitude = in.readString();
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
