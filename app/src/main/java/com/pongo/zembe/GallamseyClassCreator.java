package com.pongo.zembe;

import org.json.JSONObject;

public class GallamseyClassCreator {

  public static PersonInChat createPersonInChatFromObject (JSONObject data){
    if(data == null) return null;
    PersonInChat person = new PersonInChat();
    try{
      String name = (String) data.get(GClassFields.USER_NAME);
      String picURL = (String) data.get(GClassFields.PROFILE_PICTURE_URL);
      String lastSeen = (String) data.get(GClassFields.LAST_SEEN);
      String userID = (String) data.get(GClassFields.USER_PLATFORM_ID);
      long lastSeenMilli = (long) data.get(GClassFields.LAST_SEEN_IN_MILLI);
      person.setLastSeen(lastSeen);
      person.setUserPlatformID(userID);
      person.setUserName(name);
      person.setProfilePictureURL(picURL);
      person.setLastSeenInMilli(lastSeenMilli);
      return person;

    }catch (Exception e){
      e.printStackTrace();
    }

    return null ;
  }
}
