package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class CreateNewErrand extends AppCompatActivity {
  Toolbar toolbar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_new_errand);
    toolbar = findViewById(R.id.app_default_toolbar);
    setSupportActionBar(toolbar);
  }
}
