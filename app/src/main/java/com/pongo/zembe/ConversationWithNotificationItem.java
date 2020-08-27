package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class ConversationWithNotificationItem implements Parcelable {
  ConversationListItem conversationListItem;
  MsgNotificationTracker notification ;


  public ConversationWithNotificationItem() {
  }


  public ConversationListItem getConversationListItem() {
    return conversationListItem;
  }

  public void setConversationListItem(ConversationListItem conversationListItem) {
    this.conversationListItem = conversationListItem;
  }

  public MsgNotificationTracker getNotification() {
    return notification;
  }

  public void setNotification(MsgNotificationTracker notification) {
    this.notification = notification;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.conversationListItem, flags);
    dest.writeParcelable(this.notification, flags);
  }

  protected ConversationWithNotificationItem(Parcel in) {
    this.conversationListItem = in.readParcelable(ConversationListItem.class.getClassLoader());
    this.notification = in.readParcelable(MsgNotificationTracker.class.getClassLoader());
  }

  public static final Creator<ConversationWithNotificationItem> CREATOR = new Creator<ConversationWithNotificationItem>() {
    @Override
    public ConversationWithNotificationItem createFromParcel(Parcel source) {
      return new ConversationWithNotificationItem(source);
    }

    @Override
    public ConversationWithNotificationItem[] newArray(int size) {
      return new ConversationWithNotificationItem[size];
    }
  };
}
