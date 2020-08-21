package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * This is class is essentially the same as "ConversationStream" except, it comes without all the messages
 * in the chat, so it is lighter
 */
public class ConversationListItem implements Parcelable {
  private String conversationStreamID ;
  private PersonInChat author = new PersonInChat();
  private PersonInChat otherPerson = new PersonInChat();
  private ArrayList<String> involvedParties = new ArrayList<>();
  private String conversationContext ;
  private Errand relatedErrand;
  private String createdAt;
  private int unReadMsgs = 0;


  public ConversationListItem() {
  }


  public int getUnReadMsgs() {
    return unReadMsgs;
  }


  public void setUnReadMsgs(int unReadMsgs) {
    this.unReadMsgs = unReadMsgs;
  }

  public PersonInChat getAuthor() {
    return author;
  }

  public void setAuthor(PersonInChat author) {
    this.author = author;
  }

  public PersonInChat getOtherPerson() {
    return otherPerson;
  }

  public void setOtherPerson(PersonInChat otherPerson) {
    this.otherPerson = otherPerson;
  }

  public String getConversationContext() {
    return conversationContext;
  }



  public void setConversationContext(String conversationContext) {
    this.conversationContext = conversationContext;
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
    dest.writeParcelable(this.author, flags);
    dest.writeParcelable(this.otherPerson, flags);
    dest.writeStringList(this.involvedParties);
    dest.writeString(this.conversationContext);
    dest.writeParcelable(this.relatedErrand, flags);
    dest.writeString(this.createdAt);
    dest.writeInt(this.unReadMsgs);
  }

  protected ConversationListItem(Parcel in) {
    this.conversationStreamID = in.readString();
    this.author = in.readParcelable(PersonInChat.class.getClassLoader());
    this.otherPerson = in.readParcelable(PersonInChat.class.getClassLoader());
    this.involvedParties = in.createStringArrayList();
    this.conversationContext = in.readString();
    this.relatedErrand = in.readParcelable(Errand.class.getClassLoader());
    this.createdAt = in.readString();
    this.unReadMsgs = in.readInt();
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
