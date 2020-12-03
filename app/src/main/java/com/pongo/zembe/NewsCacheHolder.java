package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class NewsCacheHolder implements Parcelable {
  private ArrayList<GenericErrandClass> news = new ArrayList<>();
  private int howMany;

  public NewsCacheHolder(){}
  public NewsCacheHolder(ArrayList<GenericErrandClass> news) {
    this.news = news;
  }

  public ArrayList<GenericErrandClass> getNews() {
    return news;
  }

  public void addOneNewsItem (GenericErrandClass errand){
    this.news.add(errand);
    this.howMany = this.news.size();
  }
  public void setNews(ArrayList<GenericErrandClass> news) {
    this.news = news;
    this.howMany = news.size();
  }

  public int getHowMany() {
    return howMany;
  }

  @Override
  public String toString() {
    return "NewsCacheHolder{" +
      "news=" + news +
      ", howMany=" + howMany +
      '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(this.news);
    dest.writeInt(this.howMany);
  }

  protected NewsCacheHolder(Parcel in) {
    this.news = in.createTypedArrayList(GenericErrandClass.CREATOR);
    this.howMany = in.readInt();
  }

  public static final Parcelable.Creator<NewsCacheHolder> CREATOR = new Parcelable.Creator<NewsCacheHolder>() {
    @Override
    public NewsCacheHolder createFromParcel(Parcel source) {
      return new NewsCacheHolder(source);
    }

    @Override
    public NewsCacheHolder[] newArray(int size) {
      return new NewsCacheHolder[size];
    }
  };
}
