package com.pongo.zembe;

import java.util.HashMap;

/**
 * This class is used to track all communities that are under a country
 * and have been loaded into the app already.
 * With this object, there will be no unnecessary reads from firestore if the user has already
 * looked up a country's communities
 */
public class LoadedCommunities {
  private HashMap<String, Communities> loadedCommunities = new HashMap<>();

  public LoadedCommunities(){}

  public HashMap<String, Communities> getLoadedCommunities() {
    return loadedCommunities;
  }

  public void setLoadedCommunities(HashMap<String, Communities> loadedCommunities) {
    this.loadedCommunities = loadedCommunities;
  }

  public void addCommunity(String country, Communities comms){
    loadedCommunities.put(country,comms);
  }

  public Communities getSavedCommunitiesForCountry(String country){
    return loadedCommunities.get(country);
  }

  @Override
  public String toString() {
    return "LoadedCommunities{" +
      "loadedCommunities=" + loadedCommunities +
      '}';
  }
}
