package com.pongo.zembe;

import java.sql.Timestamp;

/**
 * Abstract user class that all kinds of users in the application will extend from
 */
public abstract class User {

  private String preferredName,uniqueUserName,email, DOB, phoneNumber, whatsappNumber, uniqueID;
  private Timestamp ts = new Timestamp(System.currentTimeMillis());

  public User() {} //no-arg constructor because of Firebase

  public User(String preferredName, String DOB,String email, String phoneNumber, String whatsappNumber, String uniqueID) {
    this.preferredName = preferredName;
    this.phoneNumber = phoneNumber;
    this.whatsappNumber = whatsappNumber;
    this.uniqueID = uniqueID;
    this.DOB = DOB;
    this.email   = email;
    this.uniqueUserName =ts.getTime() +"-"+ email.split("@")[0];
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

  public void setUniqueID(String uniqueID) {
    this.uniqueID = uniqueID;
  }
}
