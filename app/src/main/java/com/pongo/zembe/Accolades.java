package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

public class Accolades implements Parcelable {
  public static final String fiveStarKey = "five-stars";
  public static final String fourStarKey = "four-stars";
  public static final String threeStarKey = "three-stars";
  public static final String twoStarKey = "two-stars";
  public static final String oneStarKey = "one-star";
  public static final String reportsKey = "reports";
  public static final String pointsKey = "points";
  public static final String errandCountKey = "no-of-errands";
  public static final String gigsCountKey = "no-of-gigs";
  private int fiveStarCount;
  private int fourStarCount;
  private int threeStarCount;
  private int twoStarCount;
  private int oneStarCount;
  private int numberOfReports;
  private int numberOfPoints;
  private int errandCount = 0;// number of errands a user HAS EVER created before, doesn't matter if they shut it down later
  private int gigsCount = 0;

  public Accolades() {
  }

  public Accolades(Accolades old) {
    this.setFiveStarCount(old.getFiveStarCount());
    this.setFourStarCount(old.getFourStarCount());
    this.setThreeStarCount(old.getThreeStarCount());
    this.setTwoStarCount(old.getTwoStarCount());
    this.setOneStarCount(old.getOneStarCount());
    this.setNumberOfPoints(old.getNumberOfPoints());
    this.setNumberOfReports(old.getNumberOfReports());
    this.setErrandCount(old.getErrandCount());
    this.setGigsCount(old.getGigsCount());

  }

  public void increaseFieldBy(String key, int value) {
    switch (key) {
      case fiveStarKey: {
        setFiveStarCount(fiveStarCount + value);
        break;
      }
      case fourStarKey: {
        setFourStarCount(fourStarCount + value);
        break;
      }
      case threeStarKey: {
        setThreeStarCount(threeStarCount + value);
        break;
      }
      case twoStarKey: {
        setTwoStarCount(twoStarCount + value);
        break;
      }
      case oneStarKey: {
        setOneStarCount(oneStarCount + value);
        break;
      }
      case reportsKey: {
        setNumberOfReports(numberOfReports + value);
        break;
      }
      case gigsCountKey: {
        setGigsCount(gigsCount + value);
        break;
      }
      case pointsKey: {
        setNumberOfPoints(numberOfPoints + value);
        break;
      }case errandCountKey: {
        setErrandCount(errandCount + value);
        break;
      }
    }
  }

  public int getGigsCount() {
    return gigsCount;
  }

  public void setGigsCount(int gigsCount) {
    this.gigsCount = gigsCount;
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
      ", gigsCount=" + gigsCount +
      '}';
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
    dest.writeInt(this.gigsCount);
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
    this.gigsCount = in.readInt();
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
}
