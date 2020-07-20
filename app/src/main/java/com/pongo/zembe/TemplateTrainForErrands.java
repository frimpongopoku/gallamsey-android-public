package com.pongo.zembe;

import java.util.ArrayList;

public class TemplateTrainForErrands {
  private ArrayList<GenericErrandClass> errands = new ArrayList<>();



  public ArrayList<GenericErrandClass> getErrands() {
    return errands;
  }

  public void setErrands(ArrayList<GenericErrandClass> errands) {
    this.errands = errands;
  }

  public void addToArray(GenericErrandClass errand, GalInterfaceGuru.TemplatingLimitExceededError limiter) {
    if (errands.size() < 16) {
      errands.add(errand);
    } else {
      limiter.callback("You already have 15 templates, you cant save anymore. Remove old templates to make space");
    }
  }

  public void removeFromArray(GenericErrandClass errand) {
    errands.remove(errand);
  }

  @Override
  public String toString() {
    return "TemplateTrainForErrands{" +
      "errands=" + errands +
      '}';
  }
}
