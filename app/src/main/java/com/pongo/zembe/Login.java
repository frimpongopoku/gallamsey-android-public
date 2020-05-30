package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

  EditText emailBox, passwordBox;
  FirebaseAuth mAuth;
  String email, password;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    mAuth = FirebaseAuth.getInstance();
    emailBox = findViewById(R.id.log_email);
    passwordBox = findViewById(R.id.log_password);
    Button loginButton = findViewById(R.id.login_btn);
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        doLogin(emailBox,passwordBox);
      }
    });
  }

  public void goToUserHomepage(){
    Intent homepage   = new Intent(this, Home.class);
    finish();
    startActivity(homepage);
  }

  public void doLogin(EditText emailBox, EditText passwordBox) {
    email = emailBox.getText().toString().trim();
    password = passwordBox.getText().toString().trim();
    Boolean emailGood = false, passwordGood = false;
    //validate email content
    //----------------------

    if (email.isEmpty()) {
      emailBox.setError("You cannot sign in without an email");
      emailBox.requestFocus();
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      emailBox.setError("Please provide a valid email address ");
      emailBox.requestFocus();
    } else {
      emailGood = true;
    }

    //validate password content
    //------------------------
    if(password.isEmpty()){
      passwordBox.setError("Please provide a valid password to your account");
      passwordBox.requestFocus();
    }else{
      passwordGood = true;
    }

    if(emailGood && passwordGood){
      mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if(task.isSuccessful()){
            Toast.makeText(Login.this, "Nice, good to go!", Toast.LENGTH_SHORT).show();
            goToUserHomepage();
          }else{
            Toast.makeText(Login.this, "Oops!"+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
          }
        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.d("Login attempt::::> ", e.getMessage());
          Toast.makeText(Login.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
      });
    }

  }
}
