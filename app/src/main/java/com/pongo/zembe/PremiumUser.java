package com.pongo.zembe;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class PremiumUser extends User {

  private String userType;

  public PremiumUser() {
  } //no-arg constructor because of Firebase


  public PremiumUser(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID, String userType, String geoLocation[], String gender) {
    super(preferredName, DOB, email, phoneNumber, whatsappNumber, uniqueID, geoLocation, gender);
    this.userType = userType;
  }


  public PremiumUser(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID, String userType, String gender) {
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

  protected PremiumUser(Parcel in) {
    super(in);
    this.userType = in.readString();
  }

  public static final Creator<PremiumUser> CREATOR = new Creator<PremiumUser>() {
    @Override
    public PremiumUser createFromParcel(Parcel source) {
      return new PremiumUser(source);
    }

    @Override
    public PremiumUser[] newArray(int size) {
      return new PremiumUser[size];
    }
  };
}
