package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Home extends AppCompatActivity {
  ArrayList<String> desc = new ArrayList<>();
  ArrayList<String> profits = new ArrayList<>();
  ArrayList<String> costs = new ArrayList<>();
  ArrayList<String> dates = new ArrayList<>();

  User authenticatedUser;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    fillInTheBlankSpaces();
    //getAuthenticated User
     if( getIntent().getSerializableExtra("authUser") instanceof GroundUser ){
       authenticatedUser = (GroundUser) getIntent().getSerializableExtra("authUser");
       Log.w("I am the user: > "+ ((GroundUser) authenticatedUser).getUserType()+":", authenticatedUser.toString());
     }
     else if(getIntent().getSerializableExtra("authUser") instanceof PremiumUser){
       authenticatedUser = (PremiumUser) getIntent().getSerializableExtra("authUser");
       Log.w("I am the user: > "+ ((PremiumUser) authenticatedUser).getUserType()+":", authenticatedUser.toString());
     }


//   ----Set default home fragment: HomePage
    Fragment default_fragment = new HomeFragment(changeToHash());
    getSupportFragmentManager().beginTransaction().replace(R.id.app_frame_layout,default_fragment).commit();

//    Set Fragment Listener to switch pages
    BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
    bottomNav.setOnNavigationItemSelectedListener(navListener);

  }
  private HashMap<String, ArrayList<String>> changeToHash(){
    HashMap<String, ArrayList<String>> map = new HashMap<>();
    map.put("descs",desc);
    map.put("profits",profits);
    map.put("costs",costs);
    map.put("dates",dates);
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
        case R.id.nav_profile:
          destinationPage = new MessagesFragment();
          break;
      }

      getSupportFragmentManager().beginTransaction().replace(R.id.app_frame_layout,destinationPage).commit();
      return true;
    }
  };

  private void fillInTheBlankSpaces(){
    desc.add("I am the first I am the first I am the first I am the first I am the first I am the first I am the first I am the first");
    desc.add("I am the second I am the first I am the first I am the first I am the first I am the first ");
    desc.add("I am the third");
    desc.add("I am the fourth");
    desc.add("I am the fifth");
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
}
