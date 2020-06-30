package com.pongo.zembe;

public class GallamseyLocationComponent {
  private String locationName, longitude, latitude;

  public GallamseyLocationComponent(String locationName, String longitude, String latitude) {
    this.locationName = locationName;
    this.longitude = longitude;
    this.latitude = latitude;
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
}
