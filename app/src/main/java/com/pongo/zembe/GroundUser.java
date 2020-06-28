package com.pongo.zembe;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class GroundUser extends User {

  private String userType;


  public GroundUser() {
  } //no-arg constructor because of Firebase

  public GroundUser(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID, String userType, String geoLocation[], String gender) {
    super(preferredName, DOB, email, phoneNumber, whatsappNumber, uniqueID, geoLocation, gender);
    this.userType = userType;
  }


  public GroundUser(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID, String userType, String gender) {
    super(preferredName, DOB, email, phoneNumber, whatsappNumber, uniqueID, gender);
    this.userType = userType;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(this.userType);
  }

  protected GroundUser(Parcel in) {
    super(in);
    this.userType = in.readString();
  }

  public static final Creator<GroundUser> CREATOR = new Creator<GroundUser>() {
    @Override
    public GroundUser createFromParcel(Parcel source) {
      return new GroundUser(source);
    }

    @Override
    public GroundUser[] newArray(int size) {
      return new GroundUser[size];
    }
  };
}
