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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateNewErrand extends AppCompatActivity implements OnDetailItemsClick {
  Toolbar toolbar;
  Button teachMe, addDetails;
  Switch imageSwitcher;
  ImageView errandImageHolder;
  LinearLayoutManager manager;
  RecyclerView recyclerView;
  DetailsListAdapter detailsListAdapter;
  ArrayList<String> detailsArray;
  EditText detailsBox;


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
//    -----------------------------------------------------

    teachMe = findViewById(R.id.errand_creation_info);
    teachMe.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent teachMePage = new Intent(CreateNewErrand.this, TeachMeHowToCreateAnErrand.class);
        startActivity(teachMePage);
      }
    });
//    -----------------------------------------------------
    detailsArray = new ArrayList<>();
    populateArray();
    detailsListAdapter = new DetailsListAdapter(this,detailsArray,this);
    recyclerView = findViewById(R.id.details_recycler_list);
    manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(detailsListAdapter);
//  -----------------------------------------------------
    detailsBox = findViewById(R.id.details_textbox);
    addDetails = findViewById(R.id.add_details_btn);
    addDetails.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String val = detailsBox.getText().toString();
        if(!val.isEmpty()){
          detailsArray.add(val);
          detailsListAdapter.notifyDataSetChanged();
          detailsBox.setText("");
        }
      }
    });
  }

  public void populateArray(){
    detailsArray.add("My helper oooo, My helper");
    detailsArray.add("My helper oooo, My helper2");
    detailsArray.add("My helper oooo, My helper3");

  }

  @Override
  public void onDetailItemClick(int pos) {
    detailsArray.remove(pos);
    detailsListAdapter.notifyDataSetChanged();
    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    detailsBox.requestFocus();
  }
}
