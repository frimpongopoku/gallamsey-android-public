package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Errand implements Parcelable {

  private String title, errandType, description,
    createdAt = DateHelper.getDateInMyTimezone();
  private ArrayList<String> details = new ArrayList<>(), tags = new ArrayList<>();
  private Boolean errandComplete = false, creatorHasPaidAmount = false;
  private String creatorPaymentTransactionCode;
  private float allowance = 0, cost = 0;
  private String status = Konstants.ERRAND_HAS_NOT_STARTED;
  private SimpleUser creator, runner;
  private ArrayList<SimpleUser> notifiableRiders = new ArrayList<>();
  private GallamseyLocationComponent pickUpLocation;
  private ArrayList<String> images = new ArrayList<>();
  private String errandDocumentID;
  private long expiryDate;

  public Errand() {
    //Firebase constructor
  }

  public Errand(String errandType, String description) {
    this.title = setTitle(description);
    this.errandType = errandType;
    this.description = description;
  }


  public String getTitle() {
    return title;
  }

  public String setTitle(String description) {
    String title = "";
    if (!description.trim().isEmpty()) {
      if (description.length() >= 30) {
        title = description.substring(0, 30) + "...";
      } else {
        title = description;
      }

    }
    return title;
  }


  public String getErrandDocumentID() {
    return errandDocumentID;
  }

  public void setErrandDocumentID(String errandDocumentID) {
    this.errandDocumentID = errandDocumentID;
  }

  public ArrayList<String> getImages() {
    return images;
  }

  public void setImages(ArrayList<String> images) {
    this.images = images;
  }

  public ArrayList<String> getTags() {
    return tags;
  }

  public void setTags(ArrayList<String> tags) {
    this.tags = tags;
  }

  public ArrayList<SimpleUser> getNotifiableRiders() {
    return notifiableRiders;
  }

  public void setNotifiableRiders(ArrayList<SimpleUser> notifiableRiders) {
    this.notifiableRiders = notifiableRiders;
  }

  public String getErrandType() {
    return errandType;
  }

  public void setErrandType(String errandType) {
    this.errandType = errandType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStatus() {
    return status;
  }


  public GallamseyLocationComponent getPickUpLocation() {
    return pickUpLocation;
  }

  public void setPickUpLocation(GallamseyLocationComponent pickUpLocation) {
    this.pickUpLocation = pickUpLocation;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public SimpleUser getCreator() {
    return creator;
  }

  public void setCreator(SimpleUser creator) {
    this.creator = creator;
  }

  public SimpleUser getRunner() {
    return runner;
  }

  public void setRunner(SimpleUser runner) {
    this.runner = runner;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public long getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(long expiryDate) {
    this.expiryDate = expiryDate;
  }

  public ArrayList<String> getDetails() {
    return details;
  }

  public void setDetails(ArrayList<String> details) {
    this.details = details;
  }

  public Boolean getErrandComplete() {
    return errandComplete;
  }

  public void setErrandComplete(Boolean errandComplete) {
    this.errandComplete = errandComplete;
  }

  public Boolean getCreatorHasPaidAmount() {
    return creatorHasPaidAmount;
  }

  public void setCreatorHasPaidAmount(Boolean creatorHasPaidAmount) {
    this.creatorHasPaidAmount = creatorHasPaidAmount;
  }

  public String getCreatorPaymentTransactionCode() {
    return creatorPaymentTransactionCode;
  }

  public void setCreatorPaymentTransactionCode(String creatorPaymentTransactionCode) {
    this.creatorPaymentTransactionCode = creatorPaymentTransactionCode;
  }

  public float getAllowance() {
    return allowance;
  }

  public void setAllowance(float allowance) {
    this.allowance = allowance;
  }

  public float getCost() {
    return cost;
  }

  public void setCost(float cost) {
    this.cost = cost;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.title);
    dest.writeString(this.errandType);
    dest.writeString(this.description);
    dest.writeString(this.createdAt);
    dest.writeStringList(this.details);
    dest.writeStringList(this.tags);
    dest.writeValue(this.errandComplete);
    dest.writeValue(this.creatorHasPaidAmount);
    dest.writeString(this.creatorPaymentTransactionCode);
    dest.writeFloat(this.allowance);
    dest.writeFloat(this.cost);
    dest.writeString(this.status);
    dest.writeParcelable(this.creator, flags);
    dest.writeParcelable(this.runner, flags);
    dest.writeTypedList(this.notifiableRiders);
    dest.writeParcelable(this.pickUpLocation, flags);
    dest.writeStringList(this.images);
    dest.writeString(this.errandDocumentID);
    dest.writeLong(this.expiryDate);
  }

  protected Errand(Parcel in) {
    this.title = in.readString();
    this.errandType = in.readString();
    this.description = in.readString();
    this.createdAt = in.readString();
    this.details = in.createStringArrayList();
    this.tags = in.createStringArrayList();
    this.errandComplete = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.creatorHasPaidAmount = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.creatorPaymentTransactionCode = in.readString();
    this.allowance = in.readFloat();
    this.cost = in.readFloat();
    this.status = in.readString();
    this.creator = in.readParcelable(SimpleUser.class.getClassLoader());
    this.runner = in.readParcelable(SimpleUser.class.getClassLoader());
    this.notifiableRiders = in.createTypedArrayList(SimpleUser.CREATOR);
    this.pickUpLocation = in.readParcelable(GallamseyLocationComponent.class.getClassLoader());
    this.images = in.createStringArrayList();
    this.errandDocumentID = in.readString();
    this.expiryDate = in.readLong();
  }

  public static final Creator<Errand> CREATOR = new Creator<Errand>() {
    @Override
    public Errand createFromParcel(Parcel source) {
      return new Errand(source);
    }

    @Override
    public Errand[] newArray(int size) {
      return new Errand[size];
    }
  };
}
