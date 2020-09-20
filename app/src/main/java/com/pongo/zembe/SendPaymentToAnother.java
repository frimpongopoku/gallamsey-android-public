package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SendPaymentToAnother extends AppCompatActivity {

  TextView pageName;
  ImageView optionsBtn;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_payment_to_another);
    initActivity();
  }


  private void initActivity(){
    pageName = findViewById(R.id.page_name);
    optionsBtn = findViewById(R.id.options);
    optionsBtn.setVisibility(View.GONE);
    pageName.setText("Send Money To A User");
  }
}
