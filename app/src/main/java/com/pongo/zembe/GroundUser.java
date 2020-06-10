package com.pongo.zembe;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class GroundUser extends User {

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
}
