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
 * <p> user class that all kinds of users in the application will extend from
 * Implements serializable because we want to be able to move this user obj in between activities
 * for that to happen, the obj is going to be serialized before sending and unserialized when its being retrieved
 * hence, cant do without "Parcelable" </p>
 */
public class User implements Parcelable {

  private String region, country, profilePictureURL=Konstants.INIT_STRING, gender, preferredName, uniqueUserName, email, dob, phoneNumber, whatsappNumber, uniqueID, userDocumentID;
  private Date ts = new Timestamp(DateHelper.getMilliSecondsFromDate(DateHelper.getDateInMyTimezone()));
  private String createdAt = DateHelper.getDateInMyTimezone();
  private ArrayList<PaymentContact> mobileNumbersForPayment = new ArrayList<>() ;
  private ArrayList<GallamseyLocationComponent> deliveryLocations = new ArrayList<>();
  private GallamseyLocationComponent geoLocation;
  private Wallet wallet = new Wallet();
  private Accolades accolades = new Accolades();

  public User() {
  } //no-arg constructor because of Firebase


  public User(String preferredName, String DOB, String email, String phoneNumber, String whatsappNumber, String uniqueID, GallamseyLocationComponent geoLocation, String gender) {
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


  public String getUserDocumentID() {
    return userDocumentID;
  }

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


  public ArrayList<GallamseyLocationComponent> getDeliveryLocations() {
    return deliveryLocations;
  }

  public void setDeliveryLocations(ArrayList<GallamseyLocationComponent> deliveryLocations) {
    this.deliveryLocations = deliveryLocations;
  }

  public ArrayList<PaymentContact> getMobileNumbersForPayment() {
    return mobileNumbersForPayment;
  }

  public void setMobileNumbersForPayment(ArrayList<PaymentContact> mobileNumbersForPayment) {
    this.mobileNumbersForPayment = mobileNumbersForPayment;
  }


  public Accolades getAccolades() {
    return accolades;
  }

  public void setAccolades(Accolades accolades) {
    this.accolades = accolades;
  }

  public Wallet getWallet() {
    return wallet;
  }

  public void setWallet(Wallet wallet) {
    this.wallet = wallet;
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

  public GallamseyLocationComponent getGeoLocation() {
    return geoLocation;
  }

  public void setGeoLocation(GallamseyLocationComponent geoLocation) {
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
      ", userDocumentID='" + userDocumentID + '\'' +
      ", ts=" + ts +
      ", createdAt='" + createdAt + '\'' +
      ", mobileNumbersForPayment=" + mobileNumbersForPayment +
      ", deliveryLocations=" + deliveryLocations +
      ", geoLocation=" + geoLocation +
      ", wallet=" + wallet +", " +
      ", accolades=" + accolades +
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
    dest.writeString(this.userDocumentID);
    dest.writeLong(this.ts != null ? this.ts.getTime() : -1);
    dest.writeString(this.createdAt);
    dest.writeTypedList(this.mobileNumbersForPayment);
    dest.writeTypedList(this.deliveryLocations);
    dest.writeParcelable(this.geoLocation, flags);
    dest.writeParcelable(this.wallet, flags);
    dest.writeParcelable(this.accolades, flags);
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
    this.userDocumentID = in.readString();
    long tmpTs = in.readLong();
    this.ts = tmpTs == -1 ? null : new Date(tmpTs);
    this.createdAt = in.readString();
    this.mobileNumbersForPayment = in.createTypedArrayList(PaymentContact.CREATOR);
    this.deliveryLocations = in.createTypedArrayList(GallamseyLocationComponent.CREATOR);
    this.geoLocation = in.readParcelable(GallamseyLocationComponent.class.getClassLoader());
    this.wallet = in.readParcelable(Wallet.class.getClassLoader());
    this.accolades = in.readParcelable(Accolades.class.getClassLoader());
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
