package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchAnythingActivity extends AppCompatActivity {

  EditText searchBox;
  Spinner equalityBox;
  ArrayAdapter<String> spinnerAdapter;
  RecyclerView searchRecycler;
  SearchResultsRecyclerAdapter searchAdapter;
  String searchCategorySelected, errandSearchMethodSelected, operationalSign = Konstants.NOT_SET, CATEGORY = "CATEGORY", ERRAND_METHOD = "ERRAND SEARCH METHOD";
  RadioGroup searchCategoryGroup;
  RadioGroup errandSearchCategoryGroup;
  LinearLayout moreOptionsBox;
  EditText cashLimitBox;
  Button clearBtn;
  ImageView backBtn;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_anything);
    initializeActivity();

  }

  private void initializeActivity() {
    backBtn = findViewById(R.id.back_btn);
    backBtn.setOnClickListener(quit);
    clearBtn = findViewById(R.id.clear);
    clearBtn.setOnClickListener(doClearing);
    cashLimitBox = findViewById(R.id.cash_limit_box);
    searchCategoryGroup = findViewById(R.id.search_category);
    searchCategoryGroup.check(searchCategoryGroup.getChildAt(0).getId()); // set "Any" as the default selection on load
    searchCategoryGroup.setOnCheckedChangeListener(chooseSearchCategory);
    errandSearchCategoryGroup = findViewById(R.id.errand_search_category);
    errandSearchCategoryGroup.setOnCheckedChangeListener(chooseErrandCategory);
    moreOptionsBox = findViewById(R.id.more_options_box);
    searchAdapter = new SearchResultsRecyclerAdapter(this, new ArrayList<Object>());
    searchRecycler = findViewById(R.id.search_recycler);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    searchRecycler.setLayoutManager(manager);
    searchRecycler.setAdapter(searchAdapter);
    spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Konstants.SEARCH_SPINNER_ELEMENTS);
    searchBox = findViewById(R.id.search_box);
    searchBox.requestFocus();
    equalityBox = findViewById(R.id.spinner);
    equalityBox.setAdapter(spinnerAdapter);
    equalityBox.setOnItemSelectedListener(chooseOperation);
    showKeyBoardOnLoad();
  }


  private String numberToCategoryString(int i) {

    switch (i) {
      case 1: {
        return Konstants.SEARCH_ANYTHING;
      }
      case 2: {
        return Konstants.SEARCH_RIDERS;
      }
      case 3: {
        return Konstants.SEARCH_ERRANDS;
      }
      case 4: {
        return Konstants.SEARCH_ANYTHING;
      }
      case 5: {
        return Konstants.SEARCH_PROFIT;
      }
      case 6: {
        return Konstants.SEARCH_COST;
      }
    }

    return Konstants.INIT_STRING;
  }

  private AdapterView.OnItemSelectedListener chooseOperation = new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      operationalSign = adapterView.getItemAtPosition(i).toString();
      Toast.makeText(SearchAnythingActivity.this, "Le Operation: " + operationalSign, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
  };
  private RadioGroup.OnCheckedChangeListener chooseSearchCategory = new RadioGroup.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
      searchCategorySelected = numberToCategoryString(i);
      if (searchCategorySelected.equals(Konstants.SEARCH_ERRANDS)) {
        moreOptionsBox.setVisibility(View.VISIBLE);
      } else {
        moreOptionsBox.setVisibility(View.GONE);
      }
    }
  };
  private RadioGroup.OnCheckedChangeListener chooseErrandCategory = new RadioGroup.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
      errandSearchMethodSelected = numberToCategoryString(i);
      Toast.makeText(SearchAnythingActivity.this, "Search method: " + i + " - " + errandSearchMethodSelected, Toast.LENGTH_SHORT).show();

    }
  };

  private View.OnClickListener quit = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      finish();
    }
  };private View.OnClickListener doClearing = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      clearAllFields();
    }
  };
  private void clearAllFields(){
    cashLimitBox.setText(Konstants.EMPTY);
    searchCategoryGroup.clearCheck();
    errandSearchCategoryGroup.clearCheck();
    searchBox.setText(Konstants.EMPTY);
    equalityBox.setSelection(0);
    moreOptionsBox.setVisibility(View.VISIBLE);

  }
  private void showKeyBoardOnLoad() {
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
  }


}
