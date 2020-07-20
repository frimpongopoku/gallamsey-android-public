package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TagCollection implements Parcelable {
  private ArrayList<String> tags = new ArrayList<>();
  private String documentID = Konstants.INIT_STRING;

  public TagCollection(){}

  public TagCollection(ArrayList<String> tags) {
    this.tags = tags;
  }

  public String getDocumentID() {
    return documentID;
  }

  public void setDocumentID(String documentID) {
    this.documentID = documentID;
  }

  public ArrayList<String> getTags() {
    return tags;
  }

  public void setTags(ArrayList<String> tags) {
    this.tags = tags;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeStringList(this.tags);
    dest.writeString(this.documentID);
  }

  protected TagCollection(Parcel in) {
    this.tags = in.createStringArrayList();
    this.documentID = in.readString();
  }

  public static final Creator<TagCollection> CREATOR = new Creator<TagCollection>() {
    @Override
    public TagCollection createFromParcel(Parcel source) {
      return new TagCollection(source);
    }

    @Override
    public TagCollection[] newArray(int size) {
      return new TagCollection[size];
    }
  };
}
