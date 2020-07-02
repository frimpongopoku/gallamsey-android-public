package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentContact implements Parcelable {


  private String contactName;
  private String contactPhoneNumber;

  public PaymentContact(){}
  public PaymentContact(String contactName, String contactPhoneNumber) {
    this.contactName = contactName;
    this.contactPhoneNumber = contactPhoneNumber;
  }

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getContactPhoneNumber() {
    return contactPhoneNumber;
  }

  public void setContactPhoneNumber(String contactPhoneNumber) {
    this.contactPhoneNumber = contactPhoneNumber;
  }

  @Override
  public String toString() {
    return "PaymentContact{" +
      "contactName='" + contactName + '\'' +
      ", contactPhoneNumber='" + contactPhoneNumber + '\'' +
      '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.contactName);
    dest.writeString(this.contactPhoneNumber);
  }

  protected PaymentContact(Parcel in) {
    this.contactName = in.readString();
    this.contactPhoneNumber = in.readString();
  }

  public static final Creator<PaymentContact> CREATOR = new Creator<PaymentContact>() {
    @Override
    public PaymentContact createFromParcel(Parcel source) {
      return new PaymentContact(source);
    }

    @Override
    public PaymentContact[] newArray(int size) {
      return new PaymentContact[size];
    }
  };
}
