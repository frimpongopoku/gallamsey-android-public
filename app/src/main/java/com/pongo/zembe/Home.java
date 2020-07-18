package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Parcelable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Home extends AppCompatActivity implements GalInterfaceGuru.TrackHomeFragmentState, HomeNewsMultiAdapter.OnNewsItemClick, GalInterfaceGuru.EditContextMenuItemListener {

  ArrayList<String> desc = new ArrayList<>();
  ArrayList<String> profits = new ArrayList<>();
  ArrayList<String> costs = new ArrayList<>();
  ArrayList<String> dates = new ArrayList<>();
  ImageView userProfileImageOnToolbar, favBtn, optionsBtn;
  FirebaseAuth mAuth = FirebaseAuth.getInstance();
  GroundUser authenticatedUser;
  Button addErrandBtn, favoritesBtn;
  GalFirebaseHelper galFirebaseHelper = new GalFirebaseHelper();
  FirebaseFirestore firestore = FirebaseFirestore.getInstance();
  CollectionReference userDB = firestore.collection(Konstants.USER_COLLECTION);
  DocumentReference userDocumentReference;
  ArrayList<GenericErrandClass> homeFragContent;
  View homeFragState;
  Context thisActivity = this;
  Fragment currentFrag;
  TagCollection tagCollection ;
  CollectionReference tagsDB = firestore.collection(Konstants.TAG_COLLECTION);


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (mAuth.getCurrentUser() != null) {
    } else {
      goToLogin();
    }
    setContentView(R.layout.activity_home);
    initializeActivity();
  }

  private void initializeActivity() {
    fillInTheBlankSpaces();
    //getAuthenticated User if they are coming from login | register
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    if (authenticatedUser != null) {
      userDocumentReference = userDB.document(authenticatedUser.getUserDocumentID());
    }
    loadTags();
    userProfileImageOnToolbar = findViewById(R.id.toolbar_img);
    userProfileImageOnToolbar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        goToProfileViewPage(view);
      }
    });

//   ----Set default home fragment: HomePage
    Fragment default_fragment = new HomeFragment(homeFragContent,homeFragState,this);
    getSupportFragmentManager().beginTransaction().replace(R.id.app_frame_layout, default_fragment).commit();

//    Set Fragment Listener to switch pages
    BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
    bottomNav.setOnNavigationItemSelectedListener(navListener);
    favBtn = findViewById(R.id.favorites);
    favBtn.setOnClickListener(viewFavorites);
    optionsBtn = findViewById(R.id.options);
    optionsBtn.setOnClickListener(goToSettings);
    favoritesBtn = findViewById(R.id.favorites_button);
    favoritesBtn.setOnClickListener(viewFavorites);
    addErrandBtn = findViewById(R.id.add_errand_button);
    addErrandBtn.setOnClickListener(addNewErrand);
  }

  private  void loadTags(){
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


  private View.OnClickListener viewFavorites = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      favoritesBtn.setAlpha(1);
      Intent fav = new Intent(getApplicationContext(), FavoritesActivity.class);
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
      startActivity(createErrandPage);
    }
  };


  private void goToTasksPage() {
    Intent tasksPage = new Intent(this, TasksActivity.class);
    startActivity(tasksPage);
  }

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
          destinationPage = new HomeFragment(homeFragContent, homeFragState,(GalInterfaceGuru.TrackHomeFragmentState) thisActivity);
          break;
        case R.id.nav_notification:
          destinationPage = new NotificationFragment();
          break;
        case R.id.nav_message:
          destinationPage = new MessagesFragment();
          break;
        case R.id.earnings:
          destinationPage = new UserEarningsFragment();
          break;
      }

      getSupportFragmentManager().beginTransaction().replace(R.id.app_frame_layout, destinationPage).commit();
      currentFrag = destinationPage;
      return true;
    }
  };

  private void fillInTheBlankSpaces() {
    desc.add("I need someone to buy me waakye 3 cedis, Mcroni 10 cedis Fish 2 cedis, and Chicken 5 cedis");
    desc.add("I need someone to buy me waakye 3 cedis, Mcroni 10 cedis Fish 2 cedis, and Chicken 5 cedis ");
    desc.add("I need someone to buy me waakye 3 cedis, Mcroni 10 cedis Fish 2 cedis, and Chicken 5 cedis");
    desc.add("I need someone to buy me waakye 3 cedis, Mcroni 10 cedis Fish 2 cedis, and Chicken 5 cedis");
    desc.add("I need someone to buy me waakye 3 cedis, Mcroni 10 cedis Fish 2 cedis, and Chicken 5 cedis");
    profits.add("40");
    profits.add("20");
    profits.add("10");
    profits.add("50");
    profits.add("25");
    costs.add("4");
    costs.add("2");
    costs.add("1");
    costs.add("7");
    costs.add("9");
    dates.add(" 22nd march 1998");
    dates.add(" 2nd January 2998");
    dates.add(" 22nd April 2098");
    dates.add(" 22nd June 2098");
    dates.add(" 22nd December 2098");
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
    Intent page = new Intent(this,ErrandViewActivity.class);
    page.putExtra(Konstants.PASS_ERRAND_AROUND,selectedErrand);
    startActivity(page);
  }

  @Override
  public void saveFragmentState(ArrayList<GenericErrandClass> news, View view) {
    homeFragState = view;
    homeFragContent = news;
  }

  @Override
  public void getErrandToBeEdited(int pos, GenericErrandClass errand) {
    Toast.makeText(thisActivity, "I have gotten the errand at pos - "+ pos, Toast.LENGTH_SHORT).show();

  }
}