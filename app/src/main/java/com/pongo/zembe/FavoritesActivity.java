package com.pongo.zembe;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.pongo.zembe.ui.main.SectionsPagerAdapter;

import java.lang.reflect.Type;

public class FavoritesActivity extends AppCompatActivity implements TemplatesRecyclerAdapter.TemplateItemClick, FavoriteRidersRecyclerAdapter.RidersItemClick {


  GroundUser authenticatedUser;
  TagCollection tagCollection = new TagCollection();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    tagCollection = getIntent().getParcelableExtra(Konstants.PASS_TAGS);
    setContentView(R.layout.activity_favorites);
    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
    sectionsPagerAdapter.setAuthenticatedUser(authenticatedUser);
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);

  }

  @Override
  public void templateClick(int position, GenericErrandClass errand) {
    Intent page = new Intent(this, NewErrandCreationPage.class);
    page.putExtra(Konstants.MODE, Konstants.FROM_TEMPLATE_MODE);
    page.putExtra(Konstants.PASS_ERRAND_AROUND, errand);
    page.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
    page.putExtra(Konstants.PASS_TAGS, tagCollection);
    startActivity(page);
    finish();
  }

  @Override
  public void onRiderClick(int position) {

  }
}