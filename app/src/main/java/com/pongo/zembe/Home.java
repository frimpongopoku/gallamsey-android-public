package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Parcelable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Home extends AppCompatActivity {
  ArrayList<String> desc = new ArrayList<>();
  ArrayList<String> profits = new ArrayList<>();
  ArrayList<String> costs = new ArrayList<>();
  ArrayList<String> dates = new ArrayList<>();
  ImageView userProfileImageOnToolbar;
  FirebaseAuth mAuth = FirebaseAuth.getInstance();
  User authenticatedUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (mAuth.getCurrentUser() != null) {
    } else {
      goToLogin();
    }
    setContentView(R.layout.activity_home);
    fillInTheBlankSpaces();
    //getAuthenticated User if they are coming from login | register
    authenticatedUser = (GroundUser) getIntent().getParcelableExtra("authUser");
    userProfileImageOnToolbar = findViewById(R.id.toolbar_img);
    userProfileImageOnToolbar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        goToProfileViewPage(view);
      }
    });

//   ----Set default home fragment: HomePage
    Fragment default_fragment = new HomeFragment(changeToHash());
    getSupportFragmentManager().beginTransaction().replace(R.id.app_frame_layout, default_fragment).commit();

//    Set Fragment Listener to switch pages
    BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
    bottomNav.setOnNavigationItemSelectedListener(navListener);

  }

  private HashMap<String, ArrayList<String>> changeToHash() {
    HashMap<String, ArrayList<String>> map = new HashMap<>();
    map.put("descs", desc);
    map.put("profits", profits);
    map.put("costs", costs);
    map.put("dates", dates);
    return map;
  }

  private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
      Fragment destinationPage = null;
      switch (menuItem.getItemId()) {
        case R.id.nav_home:
          destinationPage = new HomeFragment(changeToHash());
          break;
        case R.id.nav_settings:
          destinationPage = new SettingsFragment();
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
    startActivity(profile);
  }
}
