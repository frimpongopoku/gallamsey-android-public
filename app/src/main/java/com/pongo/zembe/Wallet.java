package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class Wallet implements Parcelable {
  private int currentBalance = 0;
  private int pin = Konstants.DEFAULT_WALLET_PIN;
  private String walletOwner = Konstants.INIT_STRING;

  public Wallet() {
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

  public String getWalletOwner() {
    return walletOwner;
  }

  public void setWalletOwner(String walletOwner) {
    this.walletOwner = walletOwner;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.currentBalance);
    dest.writeInt(this.pin);
    dest.writeString(this.walletOwner);
  }

  protected Wallet(Parcel in) {
    this.currentBalance = in.readInt();
    this.pin = in.readInt();
    this.walletOwner = in.readString();
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
