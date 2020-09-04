package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceToToken implements Parcelable {

  private String name;
  private String token ;
  public DeviceToToken() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public String toString() {
    return "DeviceToToken{" +
      "name='" + name + '\'' +
      ", token='" + token + '\'' +
      '}';
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.token);
  }

  protected DeviceToToken(Parcel in) {
    this.name = in.readString();
    this.token = in.readString();
  }

  public static final Parcelable.Creator<DeviceToToken> CREATOR = new Parcelable.Creator<DeviceToToken>() {
    @Override
    public DeviceToToken createFromParcel(Parcel source) {
      return new DeviceToToken(source);
    }

    @Override
    public DeviceToToken[] newArray(int size) {
      return new DeviceToToken[size];
    }
  };
}
