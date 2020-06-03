package com.pongo.zembe;

public class GroundUser extends  User {

  private String userType;

  public GroundUser(){} //no-arg constructor because of Firebase

  public GroundUser(String preferredName, String DOB,String email, String phoneNumber, String whatsappNumber, String uniqueID, String userType) {
    super(preferredName, DOB, email,phoneNumber, whatsappNumber, uniqueID);
    this.userType = userType;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }
}
