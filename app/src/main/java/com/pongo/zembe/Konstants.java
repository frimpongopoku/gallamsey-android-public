package com.pongo.zembe;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;

public class Konstants {

  public static final String PASS_TAGS = "TAGS";
  public static final String UPPER_TIER = "UPPER";
  public static final String LOWER_TIER = "LOWER";
  public static final String NEGATIVE = "NEGATIVE";
  public static final String POSITIVE = "POSITIVE";
  public static final String DO = "DO";
  public static final String UNDO = "UNDO";
  public static final String INIT_STRING = "";
  public static final String AUTH_USER_KEY = "authUser";
  public static final String PASS_ERRAND_AROUND = "ERRAND";
  public static final String NOT_SET = "NOT SET";
  public static final int SENDER_VIEW_TYPE = 172;
  public static final int RECEIPIENT_VIEW_TYPE = 169;
  //  public static final double sLat = 7.636029;
//  public static final double sLong = -0.4915028;
  public static final double sLat = 5.636029;
  public static final double sLong = -0.2915028;

//   LOCATION POINTS STUFF

  public static final double MAGIC_DECIMAL = 0.04; // this gives a distance that is approximately 12 mins

  //  USER STATUS
  public static final String CREATOR = "CREATOR";
  public static final String RIDER = "RIDER";

  // ERRAND CREATION CONSTANTS
  //  --------------------------
  public static final String ERRAND_PICTURES_COLLECTION = "ERRAND_PICS";
  public static final String IMG_ERRAND_CONSTANT = "_errand_img_";
  public static final String TEXT_ERRAND = ":jyon@2203:x:text:x:znump:";
  public static final String IMAGE_ERRAND = "jyon@2203:x:img:x:znump:";
  public static final String USER_TEMPLATES_TAB = "ERRAND_TEMPLATES";
  public static final String FAVORITE_RIDERS_TAB = "FAVORITE_RIDERS";
  public static final String TASKS_GIGS_TAB = "GIGS";
  public static final String TASKS_YOUR_ERRANDS_TAB = "YOUR_ERRANDS_TAB";
  public static final String CHOOSE = "CHOOSE";
  public static final String INACTIVE = "INACTIVE";
  public static final String ACTIVE = "ACTIVE";
  public static final String DESC_TAB = "DESCRIPTION";
  public static final String IMAGE_TAB = "IMAGE_TAB";
  public static final String ALLOWANCE_TAB = "ALLOWANCE_TAB";
  public static final String ESTIMATION_TAB = "ESTIMATION_TAB";
  public static final String LOCATION_TAB = "LOCATION_TAB";
  public static final String DETAILS_TAB = "DETAILS_TAB";
  public static final String TAGGING_TAB = "TAGGING_TAB";
  public static final String SELECT_RIDERS_TAB = "SELECT_RIDERS_TAB";

  //ARRAYS
  //-----------------------------

  public static ArrayList<String> DEFAULT_SPINNER_ARRAY = new ArrayList<>();
  public static ArrayList<HashMap<String, Object>> COUNTRIES_MAP = new ArrayList<>();
  public static ArrayList<String> COUNTRIES = new ArrayList<>();
  public static ArrayList<String> GH_NETWORKS = new ArrayList<>();
  public static ArrayList<String> KE_NETWORKS = new ArrayList<>();
  public static ArrayList<PaymentContact> DUMMY_PAYMENT_CONTACT = new ArrayList<>();

  static {
    DUMMY_PAYMENT_CONTACT.add(new PaymentContact("Mummy", "243983364"));
    DUMMY_PAYMENT_CONTACT.add(new PaymentContact("Pongo", "202386709"));
    DEFAULT_SPINNER_ARRAY.add("CHOOSE");
    GH_NETWORKS.add("MTN");
    GH_NETWORKS.add("AIRTEL TIGO");
    GH_NETWORKS.add("VODAFONE");
    KE_NETWORKS.add("SAFARICOM");
    KE_NETWORKS.add("TIGO");
    KE_NETWORKS.add("VODACOM");
    COUNTRIES.add("GHANA");
    COUNTRIES.add("KENYA");
    HashMap<String, Object> GHANA = new HashMap<>();
    HashMap<String, Object> KENYA = new HashMap<>();
    GHANA.put("key", "GHANA");
    GHANA.put("name", "Ghana");
    GHANA.put("currency", "GHS");
    GHANA.put("networks", GH_NETWORKS);
    KENYA.put("key", "KENYA");
    KENYA.put("name", "Kenya");
    KENYA.put("currency", "KSE");
    KENYA.put("networks", KE_NETWORKS);
    COUNTRIES_MAP.add(GHANA);
    COUNTRIES_MAP.add(KENYA);
  }

  // JUST VALUES
  //---------------
  public static final String THIRTY_MINUTES = "30 Minutes";
  public static final String ONE_HOUR = "1 Hour";
  public static final String TWO_HOURS = "2 Hours";
  public static final String THREE_HOURS = "3 Hours";
  public static final String FOUR_HOURS = "4 Hours";
  public static final String FIVE_HOURS = "5 Hours";
  public static final String SIX_HOURS = "6 Hours";
  public static final String ONE_DAY = "1 day";
  public static final String TWO_DAYS = "2 days";

  public static final String MALE = "MALE";
  public static final String FEMALE = "FEMALE";
  public static final String OTHER = "OTHER";


  // NORMAL REQUEST CODES ALL OVER THE IN ALL CORNERS OF THE APPLICATION
  //--------------------------------------------------------------------

  public static final int CHOOSE_IMAGE_REQUEST_CODE = 034;
  public static final int PERMISSION_REQUEST_CODE = 003;
  public static final int LOCATIONS_REQUEST_CODE = 003;
  public static final int GOOGLE_AUTH_TYPE = 555;
  public static final int EMAIL_AND_PASSWORD_AUTH_TYPE = 554;
  public static final int GOOGLE_SIGN_IN_CODE = 8822;
  public static final int GOOGLE_SIGN_UP_CODE = 8820;

  //USER MEMBERSHIP TYPE CONSTANTS
  //------------------------------
  public static String GROUND_USER = "xx-pong-:ground:-num--xx";
  public static String PREMIUM_USER = "xx-pong-:premium:-num--x";


  //   ERRAND STAGE VALUES
  public static String ERRAND_IS_IN_MOTION = "MOTION";
  public static String ERRAND_HAS_NOT_STARTED = "HAS_NOT_STARTED";
  public static String ERRAND_IS_COMPLETE = "IS_COMPLETE";
  public static String ERRAND_READY_FOR_PAYMENT_TRANSACTION = "PAYMENT_TRANSACTION";

  //ERRAND ERROR VALUES
  //-----------------------------------------------
  public static String ERROR_FAILED = "ERROR FAILED";
  public static String ERROR_PASSED = "ERROR PASSED";
  public static String ERROR_SEMI_PASSED = "ERROR SEMI PASSED";

  //FIREBASE COLLECTION SET CONSTANTS
  //--------------------------------
  public static String TAG_COLLECTION = "TAGS";
  public static String USER_COLLECTION = "USERS";
  public static String ERRAND_COLLECTION = "ERRANDS";
  public static String COMMUNITIES_COLLECTION = "COMMUNITIES";
  public static String PROFILE_PICTURES_COLLECTION = "PROFILE PICTURES";
}
