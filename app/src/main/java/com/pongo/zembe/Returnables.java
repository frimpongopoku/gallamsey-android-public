package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Returnables {
  @SerializedName("check_point")
  private int checkPoint;

  @SerializedName("fallback_check_point")
  private int fallbackCheckPoint;

  public Returnables() {
  }

  public int getCheckPoint() {
    return checkPoint;
  }

  public void setCheckPoint(int checkPoint) {
    this.checkPoint = checkPoint;
  }

  public int getFallbackCheckPoint() {
    return fallbackCheckPoint;
  }

  public void setFallbackCheckPoint(int fallbackCheckPoint) {
    this.fallbackCheckPoint = fallbackCheckPoint;
  }

  @Override
  public String toString() {
    return "Returnables{" +
      "checkPoint=" + checkPoint +
      ", fallbackCheckPoint=" + fallbackCheckPoint +
      '}';
  }


}
