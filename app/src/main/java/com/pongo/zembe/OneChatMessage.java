package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class OneChatMessage implements Parcelable {
  private String timeStamp;
  private long timeStampInMilli;
  private String userPlatformID;
  private String message;
  private boolean seanStatus = false;

  public OneChatMessage() {
  }

  public String getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
    if (timeStamp != null) {
      long millis = DateHelper.getMilliSecondsFromDate(timeStamp);
      setTimeStampInMilli(millis);
    }
  }

  public long getTimeStampInMilli() {
    return timeStampInMilli;
  }

  public void setTimeStampInMilli(long timeStampInMilli) {
    this.timeStampInMilli = timeStampInMilli;
  }

  public String getUserPlatformID() {
    return userPlatformID;
  }

  public void setUserPlatformID(String userPlatformID) {
    this.userPlatformID = userPlatformID;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isSeanStatus() {
    return seanStatus;
  }

  public void setSeanStatus(boolean seanStatus) {
    this.seanStatus = seanStatus;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.timeStamp);
    dest.writeLong(this.timeStampInMilli);
    dest.writeString(this.userPlatformID);
    dest.writeString(this.message);
    dest.writeByte(this.seanStatus ? (byte) 1 : (byte) 0);
  }

  protected OneChatMessage(Parcel in) {
    this.timeStamp = in.readString();
    this.timeStampInMilli = in.readLong();
    this.userPlatformID = in.readString();
    this.message = in.readString();
    this.seanStatus = in.readByte() != 0;
  }

  public static final Creator<OneChatMessage> CREATOR = new Creator<OneChatMessage>() {
    @Override
    public OneChatMessage createFromParcel(Parcel source) {
      return new OneChatMessage(source);
    }

    @Override
    public OneChatMessage[] newArray(int size) {
      return new OneChatMessage[size];
    }
  };
}
