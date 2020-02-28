package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

//    Set default home fragment: HomePage
    Fragment default_fragment = new HomeFragment();
    getSupportFragmentManager().beginTransaction().replace(R.id.app_frame_layout,default_fragment).commit();
//    Set Fragment Listener to switch pages
    BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
    bottomNav.setOnNavigationItemSelectedListener(navListener);
  }

  private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
      Fragment destinationPage = null;

      switch (menuItem.getItemId()) {
        case R.id.nav_home:
          destinationPage = new HomeFragment();
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
}
