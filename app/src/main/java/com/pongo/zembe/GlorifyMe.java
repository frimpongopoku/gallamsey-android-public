package com.pongo.zembe;

public class GlorifyMe {
  public static final int fiveStarWeight = 60;
  public static final int fourStarWeight = 40;
  public static final int threeStarWeight = 20;
  public static final int twoStarWeight = -15;
  public static final int oneStarWeight = -30;
  private Accolades accolades;
  private int goldMedals;
  private int silverMedals;
  private int bronzeMedals;
  private int junkCups;

  public GlorifyMe(Accolades accolades){
    this.setAccolades(accolades);
  }

  public Accolades getAccolades() {
    return accolades;
  }

  public void setAccolades(Accolades accolades) {
    this.accolades = accolades;
  }

  public int getGoldMedals() {
    return goldMedals;
  }

  public int getSilverMedals() {
    return silverMedals;
  }

  public int getBronzeMedals() {
    return bronzeMedals;
  }

  public int getJunkCups() {
    return junkCups;
  }
}
