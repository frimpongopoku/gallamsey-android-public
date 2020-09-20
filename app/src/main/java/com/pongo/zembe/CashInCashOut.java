package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class CashInCashOut extends AppCompatActivity {
  TextView pageName, pageKeyTextView;
  ImageView optionsBtn, upIcon, downIcon;
  Spinner mobileNumberDrop, networkDrop, countryDrop;
  ContactDropDownAdapter contactDropDownAdapter;
  String selectedCountry, selectedNetwork;
  PaymentContact selectedPaymentContact;
  Context thisActivity;
  String pageKey;
  private AdapterView.OnItemSelectedListener countrySelector = new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      String country = adapterView.getItemAtPosition(i).toString();
      selectedCountry = country;
      if (country.equals("GHANA")) {
        MyHelper.initializeDropDown(Konstants.GH_NETWORKS, networkDrop, thisActivity);
      } else if (country.equals("KENYA")) {
        MyHelper.initializeDropDown(Konstants.KE_NETWORKS, networkDrop, thisActivity);
      }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cash_in);
    initActivity();
  }

  private void initActivity() {
    pageName = findViewById(R.id.page_name);
    pageName.setText("Send Cash Into Your Wallet");
    upIcon = findViewById(R.id.up_icon);
    downIcon = findViewById(R.id.down_icon);
    pageKey = getIntent().getStringExtra(Konstants.PAGE_KEY);
    pageKey = pageKey != null ? pageKey : Konstants.CASH_IN_PAGE_KEY;
    pageKeyTextView = findViewById(R.id.page_key);
    setPageKeyContent(pageKey);
    thisActivity = this;
    optionsBtn = findViewById(R.id.options);
    optionsBtn.setVisibility(View.GONE);
    mobileNumberDrop = findViewById(R.id.payment_numbers_spinner);
    contactDropDownAdapter = new ContactDropDownAdapter(this, Konstants.DUMMY_PAYMENT_CONTACT);
    mobileNumberDrop.setAdapter(contactDropDownAdapter);
    countryDrop = findViewById(R.id.country_dropdown);
    countryDrop.setOnItemSelectedListener(countrySelector);
    networkDrop = findViewById(R.id.mobile_networks);
    MyHelper.initializeDropDown(Konstants.COUNTRIES, countryDrop, this);
    MyHelper.initializeDropDown(Konstants.GH_NETWORKS, networkDrop, this);


  }

  private void setPageKeyContent(String pageKey) {
    if (pageKey.equals(Konstants.CASH_OUT_PAGE_KEY)) {
      pageKeyTextView.setText("CASH OUT");
      downIcon.setVisibility(View.GONE);
      upIcon.setVisibility(View.VISIBLE);
      pageName.setText("Send Cash Out Of Your Wallet");
    }
  }
}
