package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class Home extends AppCompatActivity implements GalInterfaceGuru.TrackConversationListPage, ConversationListRecyclerAdapter.ConversationListItemClicked, GalInterfaceGuru.TrackHomeFragmentState, HomeNewsMultiAdapter.OnNewsItemClick, GalInterfaceGuru.EditContextMenuItemListener, GalInterfaceGuru.TrackWalletFragmentState, GalInterfaceGuru.MessageCreatorContextMenuItemListener {

  private static final String TAG = "HOME";
  ImageView favBtn, optionsBtn;
  CircleImageView userProfileImageOnToolbar;
  FirebaseAuth mAuth = FirebaseAuth.getInstance();
  GroundUser authenticatedUser;
  Button addErrandBtn, favoritesBtn;
  GalFirebaseHelper galFirebaseHelper = new GalFirebaseHelper();
  FirebaseFirestore firestore = FirebaseFirestore.getInstance();
  CollectionReference userDB = firestore.collection(Konstants.USER_COLLECTION);
  DocumentReference userDocumentReference;
  ArrayList<GenericErrandClass> homeFragContent;
  View homeFragState, walletFrag, messageFragState;
  Context thisActivity = this;
  Fragment currentFrag;
  TagCollection tagCollection;
  CollectionReference tagsDB = firestore.collection(Konstants.TAG_COLLECTION);
  ArrayList<Object> walletFragContent;
  EditText searchBox;
  BottomNavigationView bottomNav;
  GalFirebaseHelper myFirebaseHelper = new GalFirebaseHelper();
  FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  CollectionReference userCollectionRef = db.collection(Konstants.USER_COLLECTION);
  ArrayList<ConversationListItem> messageFragItems;
  Handler handler = new Handler();
  RequestQueue httpHandler;
  TextView msgNotificationBadge;
  private View.OnClickListener goToSearchActivity = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent page = new Intent(thisActivity, SearchAnythingActivity.class);
      startActivity(page);
    }
  };
  private View.OnClickListener goToProfile = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      goToProfileViewPage(view);
    }
  };
  private View.OnClickListener viewFavorites = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      favoritesBtn.setAlpha(1);
      Intent fav = new Intent(getApplicationContext(), FavoritesActivity.class);
      fav.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
      fav.putExtra(Konstants.PASS_TAGS, tagCollection);
      startActivity(fav);
    }
  };
  private View.OnClickListener goToSettings = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent settings = new Intent(getApplicationContext(), OfficialSettingsPage.class);
      settings.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
      startActivity(settings);
    }
  };
  private View.OnClickListener addNewErrand = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent createErrandPage = new Intent(getApplicationContext(), NewErrandCreationPage.class);
      createErrandPage.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
      createErrandPage.putExtra(Konstants.PASS_TAGS, tagCollection);
      createErrandPage.putExtra(Konstants.MODE, Konstants.INIT_STRING); //set it to an empty string, showing that its not edit mode
      startActivity(createErrandPage);
    }
  };
  private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
      if (menuItem.getItemId() == R.id.tasks) {
        goToTasksPage();
        return false;
      }
      Fragment destinationPage = null;
      switch (menuItem.getItemId()) {
        case R.id.nav_home:
          destinationPage = new HomeFragment(homeFragContent, homeFragState, (GalInterfaceGuru.TrackHomeFragmentState) thisActivity);
          ((HomeFragment) destinationPage).setAuthenticatedUser(authenticatedUser);
          break;
        case R.id.nav_notification:
          destinationPage = new NotificationFragment();
          break;
        case R.id.nav_message:
          destinationPage = new MessagesFragment(thisActivity);
          ((MessagesFragment) destinationPage).setAuthenticatedUser(authenticatedUser);
          ((MessagesFragment) destinationPage).setOldState(messageFragState, messageFragItems);
          break;
        case R.id.earnings:
          destinationPage = new UserEarningsFragment(thisActivity);
          ((UserEarningsFragment) destinationPage).setAuthenticatedUser(authenticatedUser);
          ((UserEarningsFragment) destinationPage).setOldState(walletFragContent, walletFrag);
          break;
      }

      getSupportFragmentManager().beginTransaction().replace(R.id.app_frame_layout, destinationPage).commit();
      currentFrag = destinationPage;
      return true;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    NotificationChannelSetup notifyMe = new NotificationChannelSetup(this);
    notifyMe.createChannels();
    if (mAuth.getCurrentUser() == null) {
      goToLogin();
    }
    setContentView(R.layout.activity_home);
    initializeActivity();
    getConversationListContentPeriodically();
  }

  private void updateMessagesNotificationBadge() {
    if (messageFragItems == null || messageFragItems.size() == 0) return;
    int count = 0;
    for (int i = 0; i < messageFragItems.size(); i++) {
      ConversationListItem conversationListItem = messageFragItems.get(i);
      int unread = conversationListItem.getUnReadMsgs();
      count += unread;
    }
    if (count > 0) {
      msgNotificationBadge.setVisibility(View.VISIBLE);
      msgNotificationBadge.setText(String.valueOf(count));
    }else{
      msgNotificationBadge.setVisibility(View.GONE);
    }
  }

  private void getConversationsFromAbove() {
    JSONObject data = new JSONObject();
    try {
      data.put("user_id", authenticatedUser.getUserDocumentID());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonObjectRequest convoRequest = new JsonObjectRequest(Request.Method.POST, GallamseyURLS.FIND_ALL_CONVERSATIONS, data, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        ArrayList<ConversationListItem> conversations = processResponseData(response);
        messageFragItems = conversations;
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse: " + error.getMessage());
      }
    });
    httpHandler.add(convoRequest);

  }

  private ArrayList<ConversationListItem> processResponseData(JSONObject response) {
    ArrayList<ConversationListItem> conversations = new ArrayList<>();
    try {
      JSONObject error = (JSONObject) response.get("error");
      JSONArray content = (JSONArray) response.get("data");
      if (content != null) {
        Gson gson = new Gson();
        for (int i = 0; i < content.length(); i++) {
          JSONObject conversation = (JSONObject) content.get(i);
          ConversationListItem convo = gson.fromJson(conversation.toString(), ConversationListItem.class);
          Log.d(TAG, "onResponse: " + convo);
          conversations.add(convo);
        }
      }
    } catch (JSONException e) {
      Log.d(TAG, "onResponse: ERORR " + e.getMessage());
    }

    return conversations;
  }

  private void getConversationListContentPeriodically() {
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        Log.d(TAG, "run: Searching for conversation list.....");
        getConversationsFromAbove();
        updateMessagesNotificationBadge();
        getConversationListContentPeriodically();
      }
    }, 4000);
  }

  private void initializeActivity() {
    msgNotificationBadge = findViewById(R.id.msg_notification_badge);
    httpHandler = Volley.newRequestQueue(this);
    searchBox = findViewById(R.id.search_box);
    searchBox.setOnClickListener(goToSearchActivity);
    userProfileImageOnToolbar = findViewById(R.id.toolbar_img);
    userProfileImageOnToolbar.setOnClickListener(goToProfile);
    //getAuthenticated User if they are coming from login | register
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    if (authenticatedUser != null) {
      userDocumentReference = userDB.document(authenticatedUser.getUserDocumentID());
      GallamseyAppInstanceChecker appInstanceChecker = new GallamseyAppInstanceChecker(authenticatedUser.getAppInstanceToken(), authenticatedUser.getUserDocumentID());
      appInstanceChecker.checkAndUpdateInstanceTokenOnServer();
      setProfilePicture();
    }
    loadTags();
    //----------------------  Set Fragment Listener to switch pages -----------------------------------------
    bottomNav = findViewById(R.id.bottom_nav);
    bottomNav.setOnNavigationItemSelectedListener(navListener);
    String defaultTab = getIntent().getStringExtra(Konstants.DEFAULT_PAGE);
    if (defaultTab != null) {
      //means homepage is being opened from notification
      // this fxn is only needed so the user's info can be collected even if they open the homepage from a notification
      //normally the user's info collection wld have been done in either login|register|or splash page and passed into this page as an argument to prevent unnecessary firestore reads
      getUserDocumentAndSetToAuth(defaultTab);
    } else {
      startFromSpecificFragment(defaultTab);

    }
    Log.d(TAG, "initializeActivity: " + defaultTab);
    //-------------------------------------------------------------------------------------------------------
    favBtn = findViewById(R.id.favorites);
    favBtn.setOnClickListener(viewFavorites);
    optionsBtn = findViewById(R.id.options);
    optionsBtn.setOnClickListener(goToSettings);
    favoritesBtn = findViewById(R.id.favorites_button);
    favoritesBtn.setOnClickListener(viewFavorites);
    addErrandBtn = findViewById(R.id.add_errand_button);
    addErrandBtn.setOnClickListener(addNewErrand);
  }

  private void getUserDocumentAndSetToAuth(final String defaultTab) {
    myFirebaseHelper.getDocumentWithField(Konstants.DB_QUERY_FIELD_UNIQUE_ID, authUser.getUid(), userCollectionRef, new GalInterfaceGuru.SnapshotTakerInterface() {
      @Override
      public void callback(DocumentSnapshot document) {
        authenticatedUser = document.toObject(GroundUser.class);
        startFromSpecificFragment(defaultTab);
      }
    });
  }

  private void startFromSpecificFragment(String which) {
    Fragment default_fragment;
    if (which != null && which.equals(Konstants.CHAT_LIST_PAGE)) {
      default_fragment = new MessagesFragment(this);
      ((MessagesFragment) default_fragment).setAuthenticatedUser(authenticatedUser);
      getSupportFragmentManager().beginTransaction().replace(R.id.app_frame_layout, default_fragment).commit();
      if (bottomNav != null) {
        bottomNav.setSelectedItemId(R.id.nav_message);
      }
      return;
    }
    //   ----Set default home fragment: HomePage if nothing is set
    default_fragment = new HomeFragment(homeFragContent, homeFragState, this);
    ((HomeFragment) default_fragment).setAuthenticatedUser(authenticatedUser);
    getSupportFragmentManager().beginTransaction().replace(R.id.app_frame_layout, default_fragment).commit();
  }

  private void setProfilePicture() {
    if (!authenticatedUser.getProfilePictureURL().equals(Konstants.INIT_STRING)) {
      //means user has a custom profile
      Picasso.get().load(authenticatedUser.getProfilePictureURL()).into(userProfileImageOnToolbar);
    } else {
      //check user gender and use to determine which default profile photo to use
      if (authenticatedUser.getGender().equals(Konstants.MALE)) {
        userProfileImageOnToolbar.setImageResource(R.drawable.african_avatar_male);
      } else if (authenticatedUser.getGender().equals(Konstants.FEMALE)) {
        userProfileImageOnToolbar.setImageResource(R.drawable.african_avatar_female);
      } else {
        userProfileImageOnToolbar.setImageResource(R.drawable.profile_dummy_box_other);
      }
    }
  }

  private void loadTags() {
    tagsDB.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot documents) {
        //this will usually just return one document
        for (DocumentSnapshot document : documents) {
          if (document.exists()) {
            tagCollection = document.toObject(TagCollection.class);
          }
        }
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        e.printStackTrace();
      }
    });
  }

  private void goToTasksPage() {
    Intent tasksPage = new Intent(this, TasksActivity.class);
    startActivity(tasksPage);
  }

  private void goToLogin() {
    Intent login = new Intent(this, Login.class);
    startActivity(login);
  }

  public void goToProfileViewPage(View v) {
    Intent profile = new Intent(this, ViewProfilePage.class);
    profile.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
    startActivity(profile);
    this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
  }

  @Override
  public void newsItemCallback(int pos, GenericErrandClass selectedErrand) {
    Intent page = new Intent(this, ErrandViewActivity.class);
    page.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
    page.putExtra(Konstants.PASS_ERRAND_AROUND, selectedErrand);
    page.putExtra(Konstants.PASS_TAGS, tagCollection);
    startActivity(page);
  }

  @Override
  public void saveFragmentState(ArrayList<GenericErrandClass> news, View view) {
    homeFragState = view;
    homeFragContent = news;
  }

  @Override
  public void getErrandToBeEdited(int pos, GenericErrandClass errand) {
    Intent page = new Intent(this, NewErrandCreationPage.class);
    page.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
    page.putExtra(Konstants.MODE, Konstants.EDIT_MODE);
    page.putExtra(Konstants.PASS_ERRAND_AROUND, errand);
    page.putExtra(Konstants.PASS_TAGS, tagCollection);
    startActivity(page);
  }


  @Override
  public void saveWalletState(ArrayList<Object> transactions, View view) {
    walletFrag = view;
    walletFragContent = transactions;
  }

  @Override
  public void talkToCreatorAboutErrand(int pos, GenericErrandClass errand) {
    Intent page = new Intent(this, ChattingPage.class);
    page.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
    page.putExtra(Konstants.PASS_ERRAND_AROUND, errand);
    startActivity(page);
  }

  @Override
  public void onConversationListItemClicked(int position, ConversationListItem item) {
    PersonInChat otherPerson;
    if (item.getAuthor().getUserPlatformID().equals(authenticatedUser.getUserDocumentID())) {
      otherPerson = item.getOtherPerson();
    } else {
      otherPerson = item.getAuthor();
    }
    Intent page = new Intent(this, ChattingPage.class);
    page.putExtra(Konstants.EXISTING_CONVERSATION, true);
    page.putExtra(Konstants.EXISTING_CONVERSATION_ID, item.getConversationStreamID());
    page.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
    page.putExtra(Konstants.USER_ON_THE_OTHER_END, otherPerson);
    page.putExtra(Konstants.PASS_ERRAND_AROUND, item.getRelatedErrand());
    startActivity(page);
  }

  @Override
  public void saveConversationListState(ArrayList<ConversationListItem> chats, View state) {
    messageFragState = state;
    // a background worker is always set to update the content of chatlist, so only overwrite content of messagFragItems
    // if it is null or has zero items and the incoming list has more items
    if (messageFragItems == null) {
      messageFragItems = chats;
    } else {
      if (chats.size() > messageFragItems.size()) {
        messageFragItems = chats;
      }
    }
  }
}