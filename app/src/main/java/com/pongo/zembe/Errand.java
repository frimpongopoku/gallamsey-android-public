package com.pongo.zembe;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Errand {
  private String title, errandType, description, expiryDate,
    createdAt = DateHelper.getDateInMyTimezone();
  private ArrayList<String> details = new ArrayList<>(), tags = new ArrayList<>();
  private Boolean errandComplete = false, creatorHasPaidAmount = false;
  private String creatorPaymentTransactionCode;
  private float allowance = 0, cost = 0;
  private String status = Konstants.ERRAND_HAS_NOT_STARTED;
  private SimpleUser creator, runner;
  private ArrayList<SimpleUser> notifiableRiders = new ArrayList<>();
  private GallamseyLocationComponent pickUpLocation;


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
    String title="";
    if (!description.trim().isEmpty()) {
      title = description.substring(0,30);
    }

    return title;
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

  public String getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(String expiryDate) {
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
}
