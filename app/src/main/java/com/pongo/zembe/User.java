package com.pongo.zembe;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Abstract user class that all kinds of users in the application will extend from
 * Implements serializable because we want to be able to move this user obj in between activities
 * for that to happen, the obj is going to be serialized before sending and unserialized when its being retrieved
 * hence, cant do without "Serializable"
 */
public abstract class User implements Serializable {

  private String preferredName, uniqueUserName, email, DOB, phoneNumber, whatsappNumber, uniqueID, geoLocation[],userDocumentID;
  private Timestamp ts = new Timestamp(System.currentTimeMillis());

  public User() {
  } //no-arg constructor because of Firebase

  public User(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID, String geoLocation[]) {
    this.preferredName = preferredName;
    this.phoneNumber = phoneNumber;
    this.whatsappNumber = whatsappNumber;
    this.uniqueID = uniqueID;
    this.DOB = DOB;
    this.email = email;
    this.uniqueUserName = ts.getTime() + "-" + email.split("@")[0];
    this.geoLocation = geoLocation;
  }

  @Exclude
  public String getUserDocumentID() {
    return userDocumentID;
  }


  @Exclude
  public void setUserDocumentID(String userDocumentID) {
    this.userDocumentID = userDocumentID;
  }

  public User(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID) {
    this.preferredName = preferredName;
    this.phoneNumber = phoneNumber;
    this.whatsappNumber = whatsappNumber;
    this.uniqueID = uniqueID;
    this.DOB = DOB;
    this.email = email;
    this.uniqueUserName = ts.getTime() + "-" + email.split("@")[0];
    this.geoLocation = geoLocation;
  }

  public String getPreferredName() {
    return preferredName;
  }

  public void setPreferredName(String preferredName) {
    this.preferredName = preferredName;
  }

  public String getDOB() {
    return DOB;
  }

  public void setDOB(String DOB) {
    this.DOB = DOB;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getUniqueUserName() {
    return uniqueUserName;
  }

  public String getWhatsappNumber() {
    return whatsappNumber;
  }

  public void setWhatsappNumber(String whatsappNumber) {
    this.whatsappNumber = whatsappNumber;
  }

  public String getUniqueID() {
    return uniqueID;
  }

  public String[] getGeoLocation() {
    return geoLocation;
  }

  public void setGeoLocation(String geoLocation[]) {
    this.geoLocation = geoLocation;
  }

  public void setUniqueID(String uniqueID) {
    this.uniqueID = uniqueID;
  }

  @Override
  public String toString() {
    return "User{" +
      "preferredName='" + preferredName + '\'' +
      ", uniqueUserName='" + uniqueUserName + '\'' +
      ", email='" + email + '\'' +
      ", DOB='" + DOB + '\'' +
      ", phoneNumber='" + phoneNumber + '\'' +
      ", whatsappNumber='" + whatsappNumber + '\'' +
      ", uniqueID='" + uniqueID + '\'' +
      ", geoLocation=" + Arrays.toString(geoLocation) +
      ", ts=" + ts +
      '}';
  }
}
