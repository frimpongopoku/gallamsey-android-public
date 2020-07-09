package com.pongo.zembe;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pongo.zembe.ui.main.SectionsPagerAdapter;

public class FavoritesActivity extends AppCompatActivity implements TempatesRecyclerAdapter.TemplateItemClick, FavoriteRidersRecyclerAdapter.RidersItemClick {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites);
    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);


  }

  @Override
  public void onClick(int position) {
    Toast.makeText(this, "Clicked + " + position, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onRiderClick(int position) {
    Toast.makeText(this, "Rider Clicked + " + position, Toast.LENGTH_SHORT).show();
  }
}