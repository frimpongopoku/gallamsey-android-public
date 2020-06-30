/**
 * <p>This is a  class that is going to be saved into errands to either represent creators or runners of the errand <br>
 *   The class is needed to minimize the number of user doc reads on Firebase while also restricting access to all of creator, or runner's data</p>
 * @param userPlatformID a unique string that can be used to trace back to the main user on the platform
 * @param userName the user's name at the time of creating | running the errand
 * @param phoneNumber phone number at the time of creating | running the errand
 * @param userPlatformType groundUser | premium user?
 * @param profilePicture URL
 * @param userStatus is the user the <b>CREATOR</b> | <b>RUNNER</b>
 * @param rating any number from 1 - 5 for either <b>CREATOR</b> | <b>RUNNER</b>
 * @return SimpleUser
 *
 * */

package com.pongo.zembe;

public class SimpleUser {

  private String userPlatformID;
  private String userName;
  private String phoneNumber;
  private String userPlatformType;
  private String profilePicture;
  private String userStatus;
  private int rating;

  public SimpleUser(String userPlatformID, String userName, String phoneNumber, String userPlatformType, String profilePicture, String userStatus) {
    this.userPlatformID = userPlatformID;
    this.userName = userName;
    this.phoneNumber = phoneNumber;
    this.userPlatformType = userPlatformType;
    this.profilePicture = profilePicture;
    this.userStatus = userStatus;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getUserPlatformID() {
    return userPlatformID;
  }

  public void setUserPlatformID(String userPlatformID) {
    this.userPlatformID = userPlatformID;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getUserPlatformType() {
    return userPlatformType;
  }

  public void setUserPlatformType(String userPlatformType) {
    this.userPlatformType = userPlatformType;
  }

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

  public String getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(String userStatus) {
    this.userStatus = userStatus;
  }
}
