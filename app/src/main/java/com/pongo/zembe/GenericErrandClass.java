package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GenericErrandClass extends Errand implements Parcelable {



  public GenericErrandClass() {
  }

  public GenericErrandClass(String errandType, String description) {
    super(errandType, description);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }

  protected GenericErrandClass(Parcel in) {
    super(in);
  }

  public static final Creator<GenericErrandClass> CREATOR = new Creator<GenericErrandClass>() {
    @Override
    public GenericErrandClass createFromParcel(Parcel source) {
      return new GenericErrandClass(source);
    }

    @Override
    public GenericErrandClass[] newArray(int size) {
      return new GenericErrandClass[size];
    }
  };


}
