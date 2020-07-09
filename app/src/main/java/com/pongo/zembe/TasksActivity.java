package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.pongo.zembe.ui.main.SectionsPagerAdapter;

public class TasksActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tasks);
    TasksSectionsPagerAdapter sectionsPagerAdapter = new TasksSectionsPagerAdapter(this, getSupportFragmentManager());
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);

  }

//  @Override
//  public void onGigItemClick(int position) {
//
//  }
//
//  @Override
//  public void onYourErrandItemClick(int position) {
//    Toast.makeText(this, "Errand item clicked: "+position, Toast.LENGTH_SHORT).show();
//  }
}
