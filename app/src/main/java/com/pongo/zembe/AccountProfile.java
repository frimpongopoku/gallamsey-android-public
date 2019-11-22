package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class AccountProfile extends AppCompatActivity {
  Toolbar toolbar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account_profile);
    toolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(toolbar);
  }
}
