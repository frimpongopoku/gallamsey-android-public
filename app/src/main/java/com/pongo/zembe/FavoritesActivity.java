package com.pongo.zembe;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.pongo.zembe.ui.main.SectionsPagerAdapter;

import java.lang.reflect.Type;

public class FavoritesActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites);
    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);
    TemplateTrainForErrands templates = (TemplateTrainForErrands) MyHelper.getFromSharedPreferences(this, Konstants.SAVE_ERRANDS_AS_TEMPLATE, TemplateTrainForErrands.class);
    if (templates != null) {
      Log.d("templatesHere:",templates.toString());
    }

  }


}