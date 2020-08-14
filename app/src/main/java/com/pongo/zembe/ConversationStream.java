package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
/**
 * Class that will represent all messages and conversation information between any two indiv... in Gallamsey
 * */
public class ConversationStream implements Parcelable {
  private String conversationID;
  private PersonInChat author = new PersonInChat();
  private PersonInChat otherPerson = new PersonInChat();
  private ArrayList<OneChatMessage> messages = new ArrayList<>();
  private Errand relatedErrand;
  private String createdAt = DateHelper.getDateInMyTimezone();

  public ConversationStream() {
  }


  public Errand getRelatedErrand() {
    return relatedErrand;
  }

  public void setRelatedErrand(Errand relatedErrand) {
    this.relatedErrand = relatedErrand;
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

  public String getConversationID() {
    return conversationID;
  }

  public void setConversationID(String conversationID) {
    this.conversationID = conversationID;
  }

  public static Creator<ConversationStream> getCREATOR() {
    return CREATOR;
  }


  public ArrayList<OneChatMessage> getMessages() {
    return messages;
  }

  public void setMessages(ArrayList<OneChatMessage> messages) {
    this.messages = messages;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.conversationID);
    dest.writeParcelable(this.author, flags);
    dest.writeParcelable(this.otherPerson, flags);
    dest.writeTypedList(this.messages);
    dest.writeParcelable(this.relatedErrand, flags);
  }

  protected ConversationStream(Parcel in) {
    this.conversationID = in.readString();
    this.author = in.readParcelable(PersonInChat.class.getClassLoader());
    this.otherPerson = in.readParcelable(PersonInChat.class.getClassLoader());
    this.messages = in.createTypedArrayList(OneChatMessage.CREATOR);
    this.relatedErrand = in.readParcelable(Errand.class.getClassLoader());
  }

  public static final Creator<ConversationStream> CREATOR = new Creator<ConversationStream>() {
    @Override
    public ConversationStream createFromParcel(Parcel source) {
      return new ConversationStream(source);
    }

    @Override
    public ConversationStream[] newArray(int size) {
      return new ConversationStream[size];
    }
  };
}
