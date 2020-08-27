package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class MsgNotificationTracker implements Parcelable {
  private String conversationStreamID = Konstants.EMPTY;
  private String chatListItemID = Konstants.EMPTY;
  private String ownerOfNotification = Konstants.EMPTY;
  private int unreadMsgs = 0;
  private String notificationDocumentID = Konstants.EMPTY;

  public MsgNotificationTracker() {
  }


  public String getConversationStreamID() {
    return conversationStreamID;
  }

  public void setConversationStreamID(String conversationStreamID) {
    this.conversationStreamID = conversationStreamID;
  }

  public String getChatListItemID() {
    return chatListItemID;
  }

  public void setChatListItemID(String chatListItemID) {
    this.chatListItemID = chatListItemID;
  }

  public String getOwnerOfNotification() {
    return ownerOfNotification;
  }

  public void setOwnerOfNotification(String ownerOfNotification) {
    this.ownerOfNotification = ownerOfNotification;
  }

  public int getUnreadMsgs() {
    return unreadMsgs;
  }

  public void setUnreadMsgs(int unreadMsgs) {
    this.unreadMsgs = unreadMsgs;
  }

  public String getNotificationDocumentID() {
    return notificationDocumentID;
  }

  public void setNotificationDocumentID(String notificationDocumentID) {
    this.notificationDocumentID = notificationDocumentID;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.conversationStreamID);
    dest.writeString(this.chatListItemID);
    dest.writeString(this.ownerOfNotification);
    dest.writeInt(this.unreadMsgs);
    dest.writeString(this.notificationDocumentID);
  }

  protected MsgNotificationTracker(Parcel in) {
    this.conversationStreamID = in.readString();
    this.chatListItemID = in.readString();
    this.ownerOfNotification = in.readString();
    this.unreadMsgs = in.readInt();
    this.notificationDocumentID = in.readString();
  }

  public static final Parcelable.Creator<MsgNotificationTracker> CREATOR = new Parcelable.Creator<MsgNotificationTracker>() {
    @Override
    public MsgNotificationTracker createFromParcel(Parcel source) {
      return new MsgNotificationTracker(source);
    }

    @Override
    public MsgNotificationTracker[] newArray(int size) {
      return new MsgNotificationTracker[size];
    }
  };
}
