package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * <p>Abstract user class that all kinds of users in the application will extend from
 * Implements serializable because we want to be able to move this user obj in between activities
 * for that to happen, the obj is going to be serialized before sending and unserialized when its being retrieved
 * hence, cant do without "Serializable" </p>
 */
public class User implements Parcelable {

  private String region, country, profilePictureURL, gender, preferredName, uniqueUserName, email, dob, phoneNumber, whatsappNumber, uniqueID, geoLocation[], userDocumentID;
  private Date ts = new Timestamp(DateHelper.getMilliSecondsFromDate(DateHelper.getDateInMyTimezone()));
  private String createdAt = DateHelper.getDateInMyTimezone();
  private ArrayList<PaymentContact> mobileNumbersForPayment;

  public User() {
  } //no-arg constructor because of Firebase


  public User(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID, String geoLocation[], String gender) {
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

  public User(String preferredName, String dob, String email, String phoneNumber, String whatsappNumber, String uniqueID, String gender) {
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


  public ArrayList<PaymentContact> getMobileNumbersForPayment() {
    return mobileNumbersForPayment;
  }

  public void setMobileNumbersForPayment(ArrayList<PaymentContact> mobileNumbersForPayment) {
    this.mobileNumbersForPayment = mobileNumbersForPayment;
  }

  public String getProfilePictureURL() {
    return profilePictureURL;
  }

  public void setProfilePictureURL(String profilePictureURL) {
    this.profilePictureURL = profilePictureURL;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
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

  public String getCreatedAt() {
    return createdAt;
  }



  @Override
  public String toString() {
    return "User{" +
      "region='" + region + '\'' +
      ", country='" + country + '\'' +
      ", profilePictureURL='" + profilePictureURL + '\'' +
      ", gender='" + gender + '\'' +
      ", preferredName='" + preferredName + '\'' +
      ", uniqueUserName='" + uniqueUserName + '\'' +
      ", email='" + email + '\'' +
      ", dob='" + dob + '\'' +
      ", phoneNumber='" + phoneNumber + '\'' +
      ", whatsappNumber='" + whatsappNumber + '\'' +
      ", uniqueID='" + uniqueID + '\'' +
      ", geoLocation=" + Arrays.toString(geoLocation) +
      ", userDocumentID='" + userDocumentID + '\'' +
      ", ts=" + ts +
      '}';
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
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
    dest.writeString(this.createdAt);
    dest.writeList(this.mobileNumbersForPayment);
  }

  protected User(Parcel in) {
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
    this.createdAt = in.readString();
    this.mobileNumbersForPayment = new ArrayList<PaymentContact>();
    in.readList(this.mobileNumbersForPayment, PaymentContact.class.getClassLoader());
  }

  public static final Creator<User> CREATOR = new Creator<User>() {
    @Override
    public User createFromParcel(Parcel source) {
      return new User(source);
    }

    @Override
    public User[] newArray(int size) {
      return new User[size];
    }
  };
}
