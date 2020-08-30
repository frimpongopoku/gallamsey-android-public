package com.pongo.zembe;

public class GlorifyMe {
  public static final int GOLD_WEIGHT = 600;
  public static final int SILVER_WEIGHT = 300;
  public static final int BRONZE_WEIGHT = 150;
  public static final int JUNK_WEIGHT = 200;
  public static final int fiveStarWeight = 60;
  public static final int fourStarWeight = 40;
  public static final int threeStarWeight = 20;
  public static final int twoStarWeight = 15;
  public static final int oneStarWeight = 30;
  private QuotientAndRemainder gold, silver, bronze, junk;
  private Accolades accolades;
  private int goldMedals;
  private int silverMedals;
  private int bronzeMedals;
  private int junkCups;

  public GlorifyMe(Accolades accolades) {
    this.setAccolades(accolades);
  }

  public Accolades getAccolades() {
    return accolades;
  }

  public void setAccolades(Accolades accolades) {
    this.accolades = accolades;
    int points = 0, pointsAgainst = 0;
    // --- Add all points  to be used to calculate medals
    points += fiveStarWeight * accolades.getFiveStarCount();
    points += fourStarWeight * accolades.getFourStarCount();
    points += threeStarWeight * accolades.getThreeStarCount();
    // ---- Add all points against , so trash cups can be calculated
    pointsAgainst += twoStarWeight * accolades.getTwoStarCount();
    pointsAgainst += oneStarWeight * accolades.getOneStarCount();
    pointsAgainst += JUNK_WEIGHT * accolades.getNumberOfReports();
    QuotientAndRemainder gold = new QuotientAndRemainder(GOLD_WEIGHT, points); // One report is the same as one junk cup
    goldMedals = gold.getQuotient();
    QuotientAndRemainder silver = new QuotientAndRemainder(SILVER_WEIGHT, gold.getRemainder());
    silverMedals = silver.getQuotient();
    QuotientAndRemainder bronze = new QuotientAndRemainder(BRONZE_WEIGHT, silver.getRemainder());
    bronzeMedals = bronze.getQuotient();
    QuotientAndRemainder trash = new QuotientAndRemainder(JUNK_WEIGHT, pointsAgainst);
    junkCups = trash.getQuotient();
    this.gold = gold;
    this.bronze = bronze;
    this.silver = silver;
    this.junk = trash;
  }
  public int getProgressToGold() {
    // how many more points until a user will get another gold medal ?
    return GOLD_WEIGHT - gold.getRemainder();
  }

  public int getGoldMedals() {
    return goldMedals;
  }

  public int getSilverMedals() {
    return SILVER_WEIGHT - silver.getRemainder();
  }

  public int getBronzeMedals() {
    return BRONZE_WEIGHT - bronze.getRemainder();
  }

  public int getJunkCups() {
    return JUNK_WEIGHT - junk.getRemainder();
  }

  class QuotientAndRemainder {
    int divider;
    int largeNumber;

    public QuotientAndRemainder(int divider, int largeNumber) {
      this.divider = divider;
      this.largeNumber = largeNumber;
    }

    public int getQuotient() {
      return largeNumber / divider;
    }

    public int getRemainder() {
      return largeNumber % divider;
    }
  }
}
