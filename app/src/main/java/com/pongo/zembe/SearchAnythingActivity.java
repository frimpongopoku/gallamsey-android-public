package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;

public class SearchAnythingActivity extends AppCompatActivity {

  EditText searchBox;
  Spinner equalityBox;
  ArrayAdapter<String> spinnerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_anything);
    initializeActivity();

  }

  private void initializeActivity() {
    spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Konstants.SEARCH_SPINNER_ELEMENTS);
    searchBox = findViewById(R.id.search_box);
    searchBox.requestFocus();
    equalityBox = findViewById(R.id.spinner);
    equalityBox.setAdapter(spinnerAdapter);
    showKeyBoardOnLoad();
  }

  private void showKeyBoardOnLoad() {
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
  }


}
