package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonInChat implements Parcelable {
  private String userPlatformID;
  private String userName;
  private String profilePictureURL;
  private String lastSeen;
  private long lastSeenInMilli;

  public PersonInChat(){}

  public String getUserPlatformID() {
    return userPlatformID;
  }

  public void setUserPlatformID(String userPlatformID) {
    this.userPlatformID = userPlatformID;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getProfilePictureURL() {
    return profilePictureURL;
  }

  public void setProfilePictureURL(String profilePictureURL) {
    this.profilePictureURL = profilePictureURL;
  }

  public String getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(String lastSeen) {
    this.lastSeen = lastSeen;
    if(lastSeen != null){
      long millis = DateHelper.getMilliSecondsFromDate(lastSeen);
      setLastSeenInMilli(millis);
    }
  }

  public long getLastSeenInMilli() {
    return lastSeenInMilli;
  }

  public void setLastSeenInMilli(long lastSeenInMilli) {
    this.lastSeenInMilli = lastSeenInMilli;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.userPlatformID);
    dest.writeString(this.userName);
    dest.writeString(this.profilePictureURL);
    dest.writeString(this.lastSeen);
    dest.writeLong(this.lastSeenInMilli);
  }

  protected PersonInChat(Parcel in) {
    this.userPlatformID = in.readString();
    this.userName = in.readString();
    this.profilePictureURL = in.readString();
    this.lastSeen = in.readString();
    this.lastSeenInMilli = in.readLong();
  }

  public static final Creator<PersonInChat> CREATOR = new Creator<PersonInChat>() {
    @Override
    public PersonInChat createFromParcel(Parcel source) {
      return new PersonInChat(source);
    }

    @Override
    public PersonInChat[] newArray(int size) {
      return new PersonInChat[size];
    }
  };

  @Override
  public String toString() {
    return "PersonInChat{" +
      "userPlatformID='" + userPlatformID + '\'' +
      ", userName='" + userName + '\'' +
      ", profilePictureURL='" + profilePictureURL + '\'' +
      ", lastSeen='" + lastSeen + '\'' +
      ", lastSeenInMilli=" + lastSeenInMilli +
      '}';
  }
}
