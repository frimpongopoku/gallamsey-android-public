package com.pongo.zembe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ImageErrandClass extends Errand {

  private ArrayList<String> images ;



  public ImageErrandClass(String title, String errandType, String description, String creatorID,  ArrayList<String> images) {
    super(title, errandType, description, creatorID);
    this.images = images;
  }
  public ArrayList<String> getImages() {
    return images;
  }

  public void setImages(ArrayList<String> images) {
    this.images = images;
  }
}
