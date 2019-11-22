package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void goToLogin(View v){
    Intent login = new Intent (this,Login.class);
    startActivity(login);
  }

  public void goToRegister(View v) {
    Intent login = new Intent(this, Register.class);
    startActivity(login);
  }

  public void goToLocationSetup(View v) {
    Intent login = new Intent(this, Location.class);
    startActivity(login);
  }
}
