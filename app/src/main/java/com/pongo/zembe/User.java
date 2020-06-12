package com.pongo.zembe;

import android.os.Parcelable;

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
public abstract class User implements  Parcelable {

  public String profilePictureURL, gender, preferredName, uniqueUserName, email, dob, phoneNumber, whatsappNumber, uniqueID, geoLocation[],userDocumentID;
  public Timestamp ts = new Timestamp(System.currentTimeMillis());

  public User() {
  } //no-arg constructor because of Firebase

  public User(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID, String geoLocation[],String gender) {
    this.preferredName = preferredName;
    this.phoneNumber = phoneNumber;
    this.whatsappNumber = whatsappNumber;
    this.uniqueID = uniqueID;
    this.dob = DOB;
    this.email = email;
    this.uniqueUserName = ts.getTime() + "-" + email.split("@")[0];
    this.geoLocation = geoLocation;
    this.gender = gender;
  }

  @Exclude
  public String getUserDocumentID() {
    return userDocumentID;
  }


  @Exclude
  public void setUserDocumentID(String userDocumentID) {
    this.userDocumentID = userDocumentID;
  }

  public User(String preferredName, String dob, String email, String phoneNumber, String whatsappNumber, String uniqueID,String gender) {
    this.preferredName = preferredName;
    this.phoneNumber = phoneNumber;
    this.whatsappNumber = whatsappNumber;
    this.uniqueID = uniqueID;
    this.dob = dob;
    this.email = email;
    this.uniqueUserName = ts.getTime() + "-" + email.split("@")[0];
    this.geoLocation = geoLocation;
    this.gender = gender;
  }

  public String getProfilePictureURL() {
    return profilePictureURL;
  }

  public void setProfilePictureURL(String profilePictureURL) {
    this.profilePictureURL = profilePictureURL;
  }

  public String getPreferredName() {
    return preferredName;
  }

  public void setPreferredName(String preferredName) {
    this.preferredName = preferredName;
  }

  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
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

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  @Override
  public String toString() {
    return "User{" +
      "preferredName='" + preferredName + '\'' +
      ", uniqueUserName='" + uniqueUserName + '\'' +
      ", email='" + email + '\'' +
      ", DOB='" + dob + '\'' +
      ", phoneNumber='" + phoneNumber + '\'' +
      ", whatsappNumber='" + whatsappNumber + '\'' +
      ", uniqueID='" + uniqueID + '\'' +
      ", geoLocation=" + Arrays.toString(geoLocation) +
      ", ts=" + ts +
      '}';
  }
}
