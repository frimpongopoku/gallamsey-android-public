package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class Accolades implements Parcelable {
  private int fiveStarCount;
  private int fourStarCount;
  private int threeStarCount;
  private int twoStarCount;
  private int oneStarCount;
  private int numberOfReports;
  private int numberOfPoints;
  private int errandCount;// number of errands a user HAS EVER created before, doesnt matter if they shut it down later

  public Accolades() {
  }

  public Accolades(Accolades old) {
    this.setFiveStarCount(old.getFiveStarCount());
    this.setFiveStarCount(old.getFourStarCount());
    this.setThreeStarCount(old.getThreeStarCount());
    this.setTwoStarCount(old.getTwoStarCount());
    this.setOneStarCount(old.getOneStarCount());
    this.setNumberOfPoints(old.getNumberOfPoints());
    this.setNumberOfReports(old.getNumberOfReports());
    this.setErrandCount(old.getErrandCount());

  }


  public int getErrandCount() {
    return errandCount;
  }

  public void setErrandCount(int errandCount) {
    this.errandCount = errandCount;
  }

  public int getFiveStarCount() {
    return fiveStarCount;
  }

  public void setFiveStarCount(int fiveStarCount) {
    this.fiveStarCount = fiveStarCount;
  }

  public int getFourStarCount() {
    return fourStarCount;
  }

  public void setFourStarCount(int fourStarCount) {
    this.fourStarCount = fourStarCount;
  }

  public int getThreeStarCount() {
    return threeStarCount;
  }

  public void setThreeStarCount(int threeStarCount) {
    this.threeStarCount = threeStarCount;
  }

  public int getTwoStarCount() {
    return twoStarCount;
  }

  public void setTwoStarCount(int twoStarCount) {
    this.twoStarCount = twoStarCount;
  }

  public int getOneStarCount() {
    return oneStarCount;
  }

  public void setOneStarCount(int oneStarCount) {
    this.oneStarCount = oneStarCount;
  }

  public int getNumberOfReports() {
    return numberOfReports;
  }

  public void setNumberOfReports(int numberOfReports) {
    this.numberOfReports = numberOfReports;
  }

  public int getNumberOfPoints() {
    return numberOfPoints;
  }

  public void setNumberOfPoints(int numberOfPoints) {
    this.numberOfPoints = numberOfPoints;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.fiveStarCount);
    dest.writeInt(this.fourStarCount);
    dest.writeInt(this.threeStarCount);
    dest.writeInt(this.twoStarCount);
    dest.writeInt(this.oneStarCount);
    dest.writeInt(this.numberOfReports);
    dest.writeInt(this.numberOfPoints);
    dest.writeInt(this.errandCount);
  }

  protected Accolades(Parcel in) {
    this.fiveStarCount = in.readInt();
    this.fourStarCount = in.readInt();
    this.threeStarCount = in.readInt();
    this.twoStarCount = in.readInt();
    this.oneStarCount = in.readInt();
    this.numberOfReports = in.readInt();
    this.numberOfPoints = in.readInt();
    this.errandCount = in.readInt();
  }

  public static final Creator<Accolades> CREATOR = new Creator<Accolades>() {
    @Override
    public Accolades createFromParcel(Parcel source) {
      return new Accolades(source);
    }

    @Override
    public Accolades[] newArray(int size) {
      return new Accolades[size];
    }
  };

  @Override
  public String toString() {
    return "Accolades{" +
      "fiveStarCount=" + fiveStarCount +
      ", fourStarCount=" + fourStarCount +
      ", threeStarCount=" + threeStarCount +
      ", twoStarCount=" + twoStarCount +
      ", oneStarCount=" + oneStarCount +
      ", numberOfReports=" + numberOfReports +
      ", numberOfPoints=" + numberOfPoints +
      ", errandCount=" + errandCount +
      '}';
  }
}
