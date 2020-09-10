package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * This class is used to model a community document that is pulled from the communities collection
 * in firestore
 */
public class Communities implements Parcelable {

  public static final Creator<Communities> CREATOR = new Creator<Communities>() {
    @Override
    public Communities createFromParcel(Parcel source) {
      return new Communities(source);
    }

    @Override
    public Communities[] newArray(int size) {
      return new Communities[size];
    }
  };
  private ArrayList<OneCommunity> Regions;

  public Communities() {
  }

  protected Communities(Parcel in) {
    this.Regions = new ArrayList<OneCommunity>();
    in.readList(this.Regions, OneCommunity.class.getClassLoader());
  }

  public ArrayList<OneCommunity> getRegions() {
    return Regions;
  }

  public void setRegions(ArrayList<OneCommunity> regions) {
    Regions = regions;
  }

  public ArrayList<String> getCommunityNames() {
    ArrayList<String> list = new ArrayList<>();
    for (int i = 0; i < Regions.size(); i++) {
      OneCommunity com = Regions.get(i);
      list.add(com.getName());
    }
    return list;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeList(this.Regions);
  }

  @Override
  public String toString() {
    return "Communities{" +
      "Regions=" + Regions +
      '}';
  }
}
