package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This is class is essentially the same as "ConversationStream" except, it comes without all the messages
 * in the chat, so it is lighter
 */
public class ConversationListItem implements Parcelable {
  private String conversationStreamID;
  private PersonInChat author = new PersonInChat();
  private ArrayList<String> involvedParties = new ArrayList<>();
  private String conversationContext;
  private GenericErrandClass relatedErrand;
  private String createdAt = DateHelper.getDateInMyTimezone();

  @SerializedName("unreadMsgs")
  private int unReadMsgs = 0;
  private String documentID;
  private Boolean isOpen;
  private String timestamp;
  private PersonInChat otherPerson = new PersonInChat();
  private OneChatMessage lastMessage = new OneChatMessage();


  public ConversationListItem() {
  }


  public OneChatMessage getLastMessage() {
    return lastMessage;
  }

  public void setLastMessage(OneChatMessage lastMessage) {
    this.lastMessage = lastMessage;
  }


  public PersonInChat getOtherPerson() {
    return otherPerson;
  }

  public void setOtherPerson(PersonInChat otherPerson) {
    this.otherPerson = otherPerson;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public Boolean getOpen() {
    return isOpen;
  }

  public void setOpen(Boolean open) {
    isOpen = open;
  }

  public int getUnReadMsgs() {
    return unReadMsgs;
  }

  public void setUnReadMsgs(int unReadMsgs) {
    this.unReadMsgs = unReadMsgs;
  }

  public String getDocumentID() {
    return documentID;
  }

  public void setDocumentID(String documentID) {
    this.documentID = documentID;
  }

  public PersonInChat getAuthor() {
    return author;
  }

  public void setAuthor(PersonInChat author) {
    this.author = author;
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

  public GenericErrandClass getRelatedErrand() {
    return relatedErrand;
  }

  public void setRelatedErrand(GenericErrandClass relatedErrand) {
    this.relatedErrand = relatedErrand;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "ConversationListItem{" +
      "conversationStreamID='" + conversationStreamID + '\'' +
      ", author=" + author +
      ", involvedParties=" + involvedParties +
      ", conversationContext='" + conversationContext + '\'' +
      ", relatedErrand=" + relatedErrand +
      ", createdAt='" + createdAt + '\'' +
      ", unReadMsgs=" + unReadMsgs +
      ", documentID='" + documentID + '\'' +
      ", isOpen=" + isOpen +
      ", timestamp='" + timestamp + '\'' +
      ", otherPerson=" + otherPerson +
      '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.conversationStreamID);
    dest.writeParcelable(this.author, flags);
    dest.writeStringList(this.involvedParties);
    dest.writeString(this.conversationContext);
    dest.writeParcelable(this.relatedErrand, flags);
    dest.writeString(this.createdAt);
    dest.writeInt(this.unReadMsgs);
    dest.writeString(this.documentID);
    dest.writeValue(this.isOpen);
    dest.writeString(this.timestamp);
    dest.writeParcelable(this.otherPerson, flags);
    dest.writeParcelable(this.lastMessage, flags);
  }

  protected ConversationListItem(Parcel in) {
    this.conversationStreamID = in.readString();
    this.author = in.readParcelable(PersonInChat.class.getClassLoader());
    this.involvedParties = in.createStringArrayList();
    this.conversationContext = in.readString();
    this.relatedErrand = in.readParcelable(GenericErrandClass.class.getClassLoader());
    this.createdAt = in.readString();
    this.unReadMsgs = in.readInt();
    this.documentID = in.readString();
    this.isOpen = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.timestamp = in.readString();
    this.otherPerson = in.readParcelable(PersonInChat.class.getClassLoader());
    this.lastMessage = in.readParcelable(OneChatMessage.class.getClassLoader());
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
