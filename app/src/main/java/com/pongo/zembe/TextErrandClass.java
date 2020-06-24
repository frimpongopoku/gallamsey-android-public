package com.pongo.zembe;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextErrandClass extends Errand {
  public TextErrandClass(String title, String errandType, String description, String creatorID, String runnerID, Date date) {
    super(title, errandType, description, creatorID, runnerID, date);
  }
}
