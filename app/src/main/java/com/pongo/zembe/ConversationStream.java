package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ConversationStream implements Parcelable {
  private String conversationID;
  private PersonInChat personOne = new PersonInChat();
  private PersonInChat personTwo = new PersonInChat();
  private ArrayList<OneChatMessage> messages = new ArrayList<>();

  public ConversationStream() {
  }

  public PersonInChat getPersonOne() {
    return personOne;
  }


  public void setPersonOne(PersonInChat personOne) {
    this.personOne = personOne;
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

  public PersonInChat getPersonTwo() {
    return personTwo;
  }

  public void setPersonTwo(PersonInChat personTwo) {
    this.personTwo = personTwo;
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
    dest.writeParcelable(this.personOne, flags);
    dest.writeParcelable(this.personTwo, flags);
    dest.writeTypedList(this.messages);
  }


  protected ConversationStream(Parcel in) {
    this.personOne = in.readParcelable(PersonInChat.class.getClassLoader());
    this.personTwo = in.readParcelable(PersonInChat.class.getClassLoader());
    this.messages = in.createTypedArrayList(OneChatMessage.CREATOR);
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
