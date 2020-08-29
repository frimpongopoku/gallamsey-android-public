package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class OfficialSettingsPage extends AppCompatActivity {

  ImageView back_btn;
  TextView addPaymentBtn, addLocationBtn, newFeatureBtn, cashInBtn, cashOutBtn;
  FirebaseAuth auth = FirebaseAuth.getInstance();
  GroundUser authenticatedUser;
  MagicBoxes dialogCreator;
  private View.OnClickListener startNewFeatureBox = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      dialogCreator.constructSimpleOneActionDialog("Suggest New Feature", "What new feature do you think would be helpfull in this app? ", "SEND", new OneAction() {
        @Override
        public void callback() {
          Toast.makeText(OfficialSettingsPage.this, "Whatever, your idea is shit!", Toast.LENGTH_SHORT).show();
        }
      }).show();
    }
  };
  private View.OnClickListener quit = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      finish();
    }
  };
  private View.OnClickListener goToPayment = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent page = new Intent(getApplicationContext(), AddMobilePaymentNumberPage.class);
      page.putExtra("authUser", authenticatedUser);
      startActivity(page);
    }
  };
  private View.OnClickListener goToLocations = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent page = new Intent(getApplicationContext(), AddMoreLocations.class);
      page.putExtra("authUser", authenticatedUser);
      startActivity(page);
    }
  };
  private View.OnClickListener goToCashInPage = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent page  = new Intent(getApplicationContext(),CashInCashOut.class);
      page.putExtra(Konstants.PAGE_KEY,Konstants.CASH_IN_PAGE_KEY);
      startActivity(page);
    }
  };private View.OnClickListener goToCashOutPage = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent page  = new Intent(getApplicationContext(),CashInCashOut.class);
      page.putExtra(Konstants.PAGE_KEY,Konstants.CASH_OUT_PAGE_KEY);
      startActivity(page);
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    setContentView(R.layout.activity_official_settings_page);
    initActivity();
  }

  private void initActivity() {
    cashInBtn = findViewById(R.id.cash_in_btn);
    cashOutBtn = findViewById(R.id.cash_out_btn);
    cashInBtn.setOnClickListener(goToCashInPage);
    cashOutBtn.setOnClickListener(goToCashOutPage);
    dialogCreator = new MagicBoxes(this);
    addPaymentBtn = findViewById(R.id.add_payment_mobile_number);
    addPaymentBtn.setOnClickListener(goToPayment);
    addLocationBtn = findViewById(R.id.add_pickup_location);
    addLocationBtn.setOnClickListener(goToLocations);
    back_btn = findViewById(R.id.back_btn);
    back_btn.setOnClickListener(quit);
    newFeatureBtn = findViewById(R.id.new_feature_btn);
    newFeatureBtn.setOnClickListener(startNewFeatureBox);
  }
}
