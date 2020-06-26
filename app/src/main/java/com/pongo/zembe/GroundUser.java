package com.pongo.zembe;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class GroundUser extends User  {

  private String userType;
  //tells the server to give you a timestamp, so you dont have to be setting it yourself
  private @ServerTimestamp
  com.google.firebase.Timestamp timestamp;

  public GroundUser() {
  } //no-arg constructor because of Firebase

  public GroundUser(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID, String userType, String geoLocation[], String gender) {
    super(preferredName, DOB, email, phoneNumber, whatsappNumber, uniqueID, geoLocation,gender);
    this.userType = userType;
  }

  public com.google.firebase.Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
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
    dest.writeString(this.userType);
    dest.writeParcelable(this.timestamp, flags);
    dest.writeString(this.region);
    dest.writeString(this.country);
    dest.writeString(this.profilePictureURL);
    dest.writeString(this.gender);
    dest.writeString(this.preferredName);
    dest.writeString(this.uniqueUserName);
    dest.writeString(this.email);
    dest.writeString(this.dob);
    dest.writeString(this.phoneNumber);
    dest.writeString(this.whatsappNumber);
    dest.writeString(this.uniqueID);
    dest.writeStringArray(this.geoLocation);
    dest.writeString(this.userDocumentID);
    dest.writeLong(this.ts != null ? this.ts.getTime() : -1);
    dest.writeList(this.mobileNumbersForPayment);
  }

  protected GroundUser(Parcel in) {
    this.userType = in.readString();
    this.timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    this.region = in.readString();
    this.country = in.readString();
    this.profilePictureURL = in.readString();
    this.gender = in.readString();
    this.preferredName = in.readString();
    this.uniqueUserName = in.readString();
    this.email = in.readString();
    this.dob = in.readString();
    this.phoneNumber = in.readString();
    this.whatsappNumber = in.readString();
    this.uniqueID = in.readString();
    this.geoLocation = in.createStringArray();
    this.userDocumentID = in.readString();
    long tmpTs = in.readLong();
    this.ts = tmpTs == -1 ? null : new Date(tmpTs);
    this.mobileNumbersForPayment = new ArrayList<PaymentContact>();
    in.readList(this.mobileNumbersForPayment, PaymentContact.class.getClassLoader());
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
