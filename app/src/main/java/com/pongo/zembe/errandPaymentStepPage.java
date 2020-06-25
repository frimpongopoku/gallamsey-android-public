package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class errandPaymentStepPage extends AppCompatActivity {
  Spinner countryDropdown, networkDropdown;
  ImageView countryFlag, networkFlag;
  TextView countryNameView, networkNameView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_errand_payment_step_page);
    networkDropdown = findViewById(R.id.network_dropdown);
    networkFlag = findViewById(R.id.network_flag);
    networkNameView = findViewById(R.id.network_name);
    countryDropdown = findViewById(R.id.country_dropdown);
    countryNameView = findViewById(R.id.country_name);
    countryFlag = findViewById(R.id.country_flag);
    initializeDropdown(Konstants.COUNTRIES, countryDropdown);
    initializeDropdown(Konstants.DEFAULT_SPINNER_ARRAY, networkDropdown);
    countryDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String country = adapterView.getItemAtPosition(i).toString();
        countryNameView.setText(country);
        setDrawableToView(countryFlag, country);
        HashMap<String, Object> countryMap = findCountry(country);
        if (countryMap != null) {
          try {
            initializeDropdown((ArrayList<String>) countryMap.get("networks"), networkDropdown);
          } catch (Exception e) {

            Log.w("networkFindingError", e.getMessage());
          }
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
    networkDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String network = adapterView.getItemAtPosition(i).toString();
        networkNameView.setText(network);
        setDrawableToView(networkFlag, network);
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });

  }


  public HashMap<String, Object> findCountry(String country) {
    HashMap<String, Object> item = null;
    for (int i = 0; i < Konstants.COUNTRIES_MAP.size(); i++) {
      item = Konstants.COUNTRIES_MAP.get(i);
      if (item.get("key").equals(country)) {
        return item;
      }
    }
    return item;
  }

  public void initializeDropdown(ArrayList<String> array, Spinner spinner) {
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, array);
    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
  }


  public void setDrawableToView(ImageView view, String which) {
    switch (which) {
      case "KENYA": {
        view.setImageResource(R.drawable.kenya);
        break;
      }
      case "GHANA": {
        view.setImageResource(R.drawable.ghana);
        break;
      }
      case "MTN": {
        view.setImageResource(R.drawable.mtn);
        break;
      }
      case "AIRTEL TIGO": {
        view.setImageResource(R.drawable.tigo);
        break;
      }
      case "VODAFONE": {
        view.setImageResource(R.drawable.vodafone);
        break;
      }case "VODACOM": {
        view.setImageResource(R.drawable.vodacom);
        break;
      }case "TIGO": {
        view.setImageResource(R.drawable.ke_tigo);
        break;
      }case "SAFARICOM": {
        view.setImageResource(R.drawable.safaricom2);
        break;
      }
    }
  }
}
