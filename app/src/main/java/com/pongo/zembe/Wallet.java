package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class Wallet implements Parcelable {
  private int currentBalance = Konstants.DEFAULT_WALLET_BALANCE_GH;
  private int pin = Konstants.DEFAULT_WALLET_PIN;
  private String walletOwnerID = Konstants.INIT_STRING;

  public Wallet() {
  }

  public Wallet(Wallet wallet){
    this.setCurrentBalance(wallet.getCurrentBalance());
    this.setPin(wallet.getPin());
    this.setWalletOwnerID(wallet.getWalletOwnerID());
  }

  public int getCurrentBalance() {
    return currentBalance;
  }

  public void setCurrentBalance(int currentBalance) {
    this.currentBalance = currentBalance;
  }

  public int getPin() {
    return pin;
  }

  public void setPin(int pin) {
    this.pin = pin;
  }

  public String getWalletOwnerID() {
    return walletOwnerID;
  }

  public void setWalletOwnerID(String walletOwnerID) {
    this.walletOwnerID = walletOwnerID;
  }

  @Override
  public String toString() {
    return "Wallet{" +
      "currentBalance=" + currentBalance +
      ", walletOwnerID='" + walletOwnerID + '\'' +
      '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.currentBalance);
    dest.writeInt(this.pin);
    dest.writeString(this.walletOwnerID);
  }

  protected Wallet(Parcel in) {
    this.currentBalance = in.readInt();
    this.pin = in.readInt();
    this.walletOwnerID = in.readString();
  }

  public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
    @Override
    public Wallet createFromParcel(Parcel source) {
      return new Wallet(source);
    }

    @Override
    public Wallet[] newArray(int size) {
      return new Wallet[size];
    }
  };
}
