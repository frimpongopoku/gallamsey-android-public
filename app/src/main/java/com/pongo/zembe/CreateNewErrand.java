package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateNewErrand extends AppCompatActivity {
  Toolbar toolbar;
  Button teachMe;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_new_errand);
    toolbar = findViewById(R.id.app_default_toolbar);
    setSupportActionBar(toolbar);

    teachMe = findViewById(R.id.teach_me);
    teachMe.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent teachMePage = new Intent(CreateNewErrand.this, TeachMeHowToCreateAnErrand.class);
        startActivity(teachMePage);
      }
    });
  }
}
