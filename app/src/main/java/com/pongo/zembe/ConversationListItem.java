package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ConversationListItem implements Parcelable {
  private String conversationStreamID ;
  private ArrayList<String> involvedParties = new ArrayList<>();
  private Errand relatedErrand;
  private String createdAt;


  public ConversationListItem() {
  }

  public String getConversationStreamID() {
    return conversationStreamID;
  }

  public void setConversationStreamID(String conversationStreamID) {
    this.conversationStreamID = conversationStreamID;
  }

  public ArrayList<String> getInvolvedParties() {
    return involvedParties;
  }

  public void setInvolvedParties(ArrayList<String> involvedParties) {
    this.involvedParties = involvedParties;
  }

  public Errand getRelatedErrand() {
    return relatedErrand;
  }

  public void setRelatedErrand(Errand relatedErrand) {
    this.relatedErrand = relatedErrand;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.conversationStreamID);
    dest.writeStringList(this.involvedParties);
    dest.writeParcelable(this.relatedErrand, flags);
    dest.writeString(this.createdAt);
  }

  protected ConversationListItem(Parcel in) {
    this.conversationStreamID = in.readString();
    this.involvedParties = in.createStringArrayList();
    this.relatedErrand = in.readParcelable(Errand.class.getClassLoader());
    this.createdAt = in.readString();
  }

  public static final Creator<ConversationListItem> CREATOR = new Creator<ConversationListItem>() {
    @Override
    public ConversationListItem createFromParcel(Parcel source) {
      return new ConversationListItem(source);
    }

    @Override
    public ConversationListItem[] newArray(int size) {
      return new ConversationListItem[size];
    }
  };
}
