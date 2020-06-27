package com.pongo.zembe;

public class PaymentContact {

  private String contactName;
  private String contactPhoneNumber;

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
}
