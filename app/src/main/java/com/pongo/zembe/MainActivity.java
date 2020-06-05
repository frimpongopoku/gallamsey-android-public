package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
  private FirebaseAuth mAuth;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mAuth = FirebaseAuth.getInstance();
  }

  public void testDate(View v){
    String errs = RandomHelpersClass.mergeTextsFromArray((ArrayList<String>) RandomHelpersClass.validateDOB("22-03-02").get("errors"));
    Toast.makeText(this, errs, Toast.LENGTH_SHORT).show();
  }
  public void logout(View v){
    try {
      mAuth.signOut();
    } catch (Exception e) {
      Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
  }
  public void goToLogin(View v){
    Intent login = new Intent (this,Login.class);
    startActivity(login);
  }
 public void compProfile(View v){
    Intent prof = new Intent (this,ProfileCompletionPage.class);
    startActivity(prof);
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
