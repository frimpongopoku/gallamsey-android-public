package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

public class CreateNewErrand extends AppCompatActivity {
  Toolbar toolbar;
  Button teachMe;
  Switch imageSwitcher;
  ImageView errandImageHolder;
  LinearLayoutManager manager;
  RecyclerView recyclerView;
  DetailsListAdapter detailsListAdapter;
  String[] detailsArray;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_new_errand);
    toolbar = findViewById(R.id.app_default_toolbar);
    setSupportActionBar(toolbar);
    errandImageHolder = findViewById(R.id.errand_image_holder);
    imageSwitcher = findViewById(R.id.image_switcher);
    imageSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
          errandImageHolder.setVisibility(View.VISIBLE);
        } else {
          errandImageHolder.setVisibility(View.GONE);
        }
      }
    });

    teachMe = findViewById(R.id.errand_creation_info);
    teachMe.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent teachMePage = new Intent(CreateNewErrand.this, TeachMeHowToCreateAnErrand.class);
        startActivity(teachMePage);
      }
    });
    detailsArray = new String[]{"All round me are familiar faces", "And then something else I dont know", "do you believe in magic?"};
    detailsListAdapter = new DetailsListAdapter(this,detailsArray);
    recyclerView = findViewById(R.id.details_recycler_list);
    manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(detailsListAdapter);
  }
}
