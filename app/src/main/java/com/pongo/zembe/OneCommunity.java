package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * This class represents on community or region that exists in a country
 */
public class OneCommunity implements Parcelable {
  private int id;
  private String name;

  public OneCommunity() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.name);
  }

  protected OneCommunity(Parcel in) {
    this.id = in.readInt();
    this.name = in.readString();
  }

  public static final Parcelable.Creator<OneCommunity> CREATOR = new Parcelable.Creator<OneCommunity>() {
    @Override
    public OneCommunity createFromParcel(Parcel source) {
      return new OneCommunity(source);
    }

    @Override
    public OneCommunity[] newArray(int size) {
      return new OneCommunity[size];
    }
  };

  @Override
  public String toString() {
    return "OneCommunity{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }
}
