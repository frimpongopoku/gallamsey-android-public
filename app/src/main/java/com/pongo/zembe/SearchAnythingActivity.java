package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchAnythingActivity extends AppCompatActivity {

  EditText searchBox;
  Spinner equalityBox;
  ArrayAdapter<String> spinnerAdapter;
  RecyclerView searchRecycler;
  SearchResultsRecyclerAdapter searchAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_anything);
    initializeActivity();

  }

  private void initializeActivity() {
    searchAdapter = new SearchResultsRecyclerAdapter(this,new ArrayList<Object>());
    searchRecycler = findViewById(R.id.search_recycler);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    searchRecycler.setLayoutManager(manager);
    searchRecycler.setAdapter(searchAdapter);
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
