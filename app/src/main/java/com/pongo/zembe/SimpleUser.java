/**
 * <p>This is a  class that is going to be saved into errands to either represent creators or runners of the errand <br>
 * The class is needed to minimize the number of user doc reads on Firebase while also restricting access to all of creator, or runner's data</p>
 *
 * @param userPlatformID a unique string that can be used to trace back to the main user on the platform
 * @param userName the user's name at the time of creating | running the errand
 * @param phoneNumber phone number at the time of creating | running the errand
 * @param userPlatformType groundUser | premium user?
 * @param profilePicture URL
 * @param userStatus is the user the <b>CREATOR</b> | <b>RUNNER</b>
 * @param rating any number from 1 - 5 for either <b>CREATOR</b> | <b>RUNNER</b>
 * @return SimpleUser
 */

package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleUser implements Parcelable {
  private String userPlatformID; // the iD of the document the represents the user's profile in USER_COLLECTION
  private String userName;
  private String phoneNumber;
  private String userPlatformType;
  private String profilePicture;
  private String userStatus;
  private GallamseyLocationComponent primaryLocation;
  private String gender;
  private String country;
  private String region;
  private int rating;

  SimpleUser() {
  }

  public SimpleUser(String userPlatformID, String userName, String phoneNumber, String userPlatformType, String profilePicture, String userStatus) {
    this.userPlatformID = userPlatformID;
    this.userName = userName;
    this.phoneNumber = phoneNumber;
    this.userPlatformType = userPlatformType;
    this.profilePicture = profilePicture;
    this.userStatus = userStatus;
  }



  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }
  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public GallamseyLocationComponent getPrimaryLocation() {
    return primaryLocation;
  }

  public void setPrimaryLocation(GallamseyLocationComponent primaryLocation) {
    this.primaryLocation = primaryLocation;
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

  @Override
  public String toString() {
    return "SimpleUser{" +
      "userPlatformID='" + userPlatformID + '\'' +
      ", userName='" + userName + '\'' +
      ", phoneNumber='" + phoneNumber + '\'' +
      ", userPlatformType='" + userPlatformType + '\'' +
      ", profilePicture='" + profilePicture + '\'' +
      ", userStatus='" + userStatus + '\'' +
      ", primaryLocation=" + primaryLocation +
      ", gender='" + gender + '\'' +
      ", rating=" + rating +
      '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.userPlatformID);
    dest.writeString(this.userName);
    dest.writeString(this.phoneNumber);
    dest.writeString(this.userPlatformType);
    dest.writeString(this.profilePicture);
    dest.writeString(this.userStatus);
    dest.writeParcelable(this.primaryLocation, flags);
    dest.writeString(this.gender);
    dest.writeString(this.country);
    dest.writeString(this.region);
    dest.writeInt(this.rating);
  }

  protected SimpleUser(Parcel in) {
    this.userPlatformID = in.readString();
    this.userName = in.readString();
    this.phoneNumber = in.readString();
    this.userPlatformType = in.readString();
    this.profilePicture = in.readString();
    this.userStatus = in.readString();
    this.primaryLocation = in.readParcelable(GallamseyLocationComponent.class.getClassLoader());
    this.gender = in.readString();
    this.country = in.readString();
    this.region = in.readString();
    this.rating = in.readInt();
  }

  public static final Creator<SimpleUser> CREATOR = new Creator<SimpleUser>() {
    @Override
    public SimpleUser createFromParcel(Parcel source) {
      return new SimpleUser(source);
    }

    @Override
    public SimpleUser[] newArray(int size) {
      return new SimpleUser[size];
    }
  };
}
