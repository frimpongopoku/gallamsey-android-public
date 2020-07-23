package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SearchAnythingActivity extends AppCompatActivity {

  EditText searchBox;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_anything);
    searchBox = findViewById(R.id.search_box);
    searchBox.requestFocus();
    showKeyBoardOnLoad();

  }


  private void showKeyBoardOnLoad() {
    getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
  }


}
