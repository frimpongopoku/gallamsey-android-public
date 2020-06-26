package com.pongo.zembe;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;

public class Konstants {


  public static final int SENDER_VIEW_TYPE = 172;
  public static final int RECEIPIENT_VIEW_TYPE = 169;
  public static final double sLat = 5.636029;
  public static final double sLong = -0.2915028;


  //ARRAYS
  //-----------------------------

  public static ArrayList<String> DEFAULT_SPINNER_ARRAY = new ArrayList<>();
  public static ArrayList<HashMap<String,Object>> COUNTRIES_MAP = new ArrayList<>();
  public static ArrayList<String> COUNTRIES = new ArrayList<>();
  public static ArrayList<String> GH_NETWORKS = new ArrayList<>();
  public static ArrayList<String> KE_NETWORKS = new ArrayList<>();
  public static ArrayList<PaymentContact> DUMMY_PAYMENT_CONTACT = new ArrayList<>();
  static {
    DUMMY_PAYMENT_CONTACT.add(new PaymentContact("Mummy","243983364"));
    DUMMY_PAYMENT_CONTACT.add(new PaymentContact("Pongo","202386709"));
    DEFAULT_SPINNER_ARRAY.add("CHOOSE");
    GH_NETWORKS.add("MTN");
    GH_NETWORKS.add("AIRTEL TIGO");
    GH_NETWORKS.add("VODAFONE");
    KE_NETWORKS.add("SAFARICOM");
    KE_NETWORKS.add("TIGO");
    KE_NETWORKS.add("VODACOM");
    COUNTRIES.add("GHANA");
    COUNTRIES.add("KENYA");
    HashMap<String,Object> GHANA = new HashMap<>();
    HashMap<String,Object> KENYA = new HashMap<>();
    GHANA.put("key","GHANA");
    GHANA.put("name","Ghana");
    GHANA.put("currency","GHS");
    GHANA.put("networks",GH_NETWORKS);
    KENYA.put("key","KENYA");
    KENYA.put("name","Kenya");
    KENYA.put("currency","KSE");
    KENYA.put("networks",KE_NETWORKS);
    COUNTRIES_MAP.add(GHANA);
    COUNTRIES_MAP.add(KENYA);
  }

  // JUST VALUES
  //---------------
  public static final String MALE = "MALE";
  public static final String FEMALE = "FEMALE";
  public static final String OTHER = "OTHER";


  // NORMAL REQUEST CODES ALL OVER THE IN ALL CORNERS OF THE APPLICATION
  //--------------------------------------------------------------------

  public static final int CHOOSE_IMAGE_REQUEST_CODE = 034;
  public static final int PERMISSION_REQUEST_CODE = 003;
  public static final int LOCATIONS_REQUEST_CODE = 003;
  public static final int GOOGLE_AUTH_TYPE = 555;
  public static final int EMAIL_AND_PASSWORD_AUTH_TYPE  =554;
  public static final int GOOGLE_SIGN_IN_CODE = 8822;
  public static final int GOOGLE_SIGN_UP_CODE = 8820;

  //USER MEMBERSHIP TYPE CONSTANTS
  //------------------------------
  public static String GROUND_USER = "xx-pong-:ground:-num--xx";
  public static String PREMIUM_USER = "xx-pong-:premium:-num--x";


  //FIREBASE COLLECTION SET CONSTANTS
  //--------------------------------
  public static String USER_COLLECTION = "USERS";
  public static String COMMUNITIES_COLLECTION = "COMMUNITIES";
  public static String PROFILE_PICTURES_COLLECTION = "PROFILE PICTURES";
}
