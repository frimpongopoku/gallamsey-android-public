package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
  private FirebaseAuth mAuth;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mAuth = FirebaseAuth.getInstance();
  }

  public void logout(View v){
    try {
      mAuth.signOut();
    } catch (Exception e) {
      Toast.makeText(this, "You are out of this shithole. Congrats! " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
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
