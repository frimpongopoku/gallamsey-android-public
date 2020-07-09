package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SplashScreen extends AppCompatActivity {

  FirebaseFirestore firestore = FirebaseFirestore.getInstance();
  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
  CollectionReference userDB = firestore.collection(Konstants.USER_COLLECTION);
  public static int SPLASH_TIME = 1000;

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
    if (user != null) {
      getUserDocument(new CollectUserInterface() {
        @Override
        public void callable(GroundUser user) {
          Intent home = new Intent(getApplicationContext(), Home.class);
          home.putExtra("authUser", user);
          startActivity(home);
          finish();
        }
      });
    } else {
      redirectToLogin();
    }
  }

  public void redirectToLogin() {
    Intent login = new Intent(this, Login.class);
    startActivity(login);
    finish();
  }

  public void getUserDocument(final CollectUserInterface collectUserInterface) {
    userDB.whereEqualTo("uniqueID", user.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot documents) {
        for (QueryDocumentSnapshot document : documents) {
          if (document.exists()) {
            GroundUser authUser = document.toObject(GroundUser.class);
            collectUserInterface.callable(authUser);
          } else {
            Log.d("CouldntFindUser", "Could not find user information");
            Toast.makeText(SplashScreen.this, "For some reason, we could not find your information", Toast.LENGTH_SHORT).show();
            redirectToLogin();
          }
        }
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.d("CouldntFindUser", e.getMessage());
      }
    });
  }

  public void goToHomepage(View view) {

    Intent home = new Intent(this, Home.class);
    startActivity(home);
  }

  interface CollectUserInterface {
    void callable(GroundUser user);
  }
}
