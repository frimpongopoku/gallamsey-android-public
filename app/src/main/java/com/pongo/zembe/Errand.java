package com.pongo.zembe;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public abstract class Errand {
  private String title, errandType, description, creatorID, runnerID, expiryDate;
  private Date date = new Date();
  private ArrayList<String> details;
  private Boolean errandComplete =false, creatorHasPaidAmount =false;
  private String creatorPaymentTransactionCode;
  private float allowance = 0, cost = 0;

  public Errand(String title, String errandType, String description, String creatorID) {
    this.title = title;
    this.errandType = errandType;
    this.description = description;
    this.creatorID = creatorID;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

  public String getCreatorID() {
    return creatorID;
  }

  public void setCreatorID(String creatorID) {
    this.creatorID = creatorID;
  }

  public String getRunnerID() {
    return runnerID;
  }

  public void setRunnerID(String runnerID) {
    this.runnerID = runnerID;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
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
