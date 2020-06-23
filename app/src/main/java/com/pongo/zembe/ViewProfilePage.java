package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewProfilePage extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_profile_page);
  }

  @Override
  public void finish() {
    super.finish();
    this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
  }
}


