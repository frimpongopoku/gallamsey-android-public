package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ErrandViewActivity extends AppCompatActivity {
  Toolbar toolbar;
  MagicBoxes dialogCreator;
  Button runErrandButton;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_errand_view);
    toolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(toolbar);
    dialogCreator  = new MagicBoxes(this);
    runErrandButton = findViewById(R.id.run_errand);
    runErrandButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.start_errand_custom_dialog,null,false);
        Dialog dialog = dialogCreator.constructCustomDialog("Confirmation", v, new MagicBoxCallables() {
          @Override
          public void negativeBtnCallable() {
            Toast.makeText(ErrandViewActivity.this, "we in the negative", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void positiveBtnCallable() {
            Toast.makeText(ErrandViewActivity.this, "We in the positive", Toast.LENGTH_SHORT).show();
          }
        });
        dialog.show();
      }
    });
  }


}
