package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

  public static int SPLASH_TIME = 3000;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        goToHomepage();
      }
    }, SPLASH_TIME);

  }

  private void goToHomepage() {
    Intent home = new Intent(this, Home.class);
    startActivity(home);
    finish();
  }

  public void goToHomepage(View view) {

    Intent home = new Intent(this, Home.class);
    startActivity(home);
  }
}
