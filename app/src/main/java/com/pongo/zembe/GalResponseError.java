package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class GalResponseError {

  private  Boolean status;
  private  String message;

  public GalResponseError(){}

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "GalResponseError{" +
      "status=" + status +
      ", message='" + message + '\'' +
      '}';
  }



}
