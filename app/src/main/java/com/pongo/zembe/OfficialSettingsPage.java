package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class OfficialSettingsPage extends AppCompatActivity {

  TextView addPaymentBtn,addLocationBtn;
  FirebaseAuth auth = FirebaseAuth.getInstance();
  GroundUser authenticatedUser;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    authenticatedUser = getIntent().getParcelableExtra("authUser");
    setContentView(R.layout.activity_official_settings_page);
    addPaymentBtn = findViewById(R.id.add_payment_mobile_number);
    addPaymentBtn.setOnClickListener(goToPayment);
    addLocationBtn = findViewById(R.id.add_pickup_location);
    addLocationBtn.setOnClickListener(goToLocations);
  }

  private View.OnClickListener goToPayment = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent page = new Intent(getApplicationContext(),AddMobilePaymentNumberPage.class);
      page.putExtra("authUser",authenticatedUser);
      startActivity(page);
    }
  };
  private View.OnClickListener goToLocations = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent page = new Intent(getApplicationContext(),AddMoreLocations.class);
      page.putExtra("authUser",authenticatedUser);
      startActivity(page);
    }
  };
}
