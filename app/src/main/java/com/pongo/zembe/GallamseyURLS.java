package com.pongo.zembe;

public class GallamseyURLS {
  public static String VERSION_ONE = "v1/";
  public static String HOST = "https://us-central1-gallamsey.cloudfunctions.net/";
  public static String LOCAL_VERSION_ONE = "http://192.168.8.152:5001/gallamsey/us-central1/v1";
  public static String FIND_PEER_TO_PEER_CONVERSATION = VERSION_ONE + "/find.my.convo";
  public static String FIND_PEER_TO_PEER_CONVERSATION_LOCAL = LOCAL_VERSION_ONE + "/find.my.convo";
  public static String TEST_URL = HOST + "test";

  public static final String FIND_ALL_CONVERSATIONS = HOST +"/conversation-finder/mine.get.all";
  public static final String REDUCE_UNREAD_MSGS = HOST +"/chat-unread/reduce.number";
  public static final String GET_MY_ERRANDS = HOST +"/find-my-errands/get.all";
  public static final String FIND_USER_PROFILE = HOST +"/profile-finder/user.find";
  public static final String GET_NEWS_CONTENT = HOST +"/news/get.my.news";
}
