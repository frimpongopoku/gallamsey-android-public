package com.pongo.zembe;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public abstract class Errand {
  private String title, errandType, description, creatorID, runnerID;
  private Date date = new Date();

  public Errand(String title, String errandType, String description, String creatorID, String runnerID, Date date) {
    this.title = title;
    this.errandType = errandType;
    this.description = description;
    this.creatorID = creatorID;
    this.runnerID = runnerID;
    this.date = date;
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
}
