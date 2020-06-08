package com.pongo.zembe;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class PremiumUser extends User {

  private String userType;
  //tells the server to give you a timestamp, so you dont have to be setting it yourself
  private @ServerTimestamp
  Timestamp timestamp;

  public PremiumUser() {
  } //no-arg constructor because of Firebase

  public PremiumUser(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID, String userType, String geoLocation[]) {
    super(preferredName, DOB, email, phoneNumber, whatsappNumber, uniqueID, geoLocation);
    this.userType = userType;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public PremiumUser(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID, String userType) {
    super(preferredName, DOB, email, phoneNumber, whatsappNumber, uniqueID);
    this.userType = userType;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }
}
