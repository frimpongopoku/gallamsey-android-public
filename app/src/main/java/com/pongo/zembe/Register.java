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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

  String username, email, password, passwordConfirmation, whatsappNumber, phone;
  EditText usernameBox, emailBox, passBox, confirmPassBox, whatsappBox, phoneBox;
  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAuth = FirebaseAuth.getInstance();
    setContentView(R.layout.activity_register);
    usernameBox = findViewById(R.id.reg_username);
    emailBox = findViewById(R.id.reg_email);
    passBox = findViewById(R.id.reg_password);
    confirmPassBox = findViewById(R.id.reg_confirm_password);
    whatsappBox = findViewById(R.id.reg_whatsapp_number);
    phoneBox = findViewById(R.id.reg_phone);

    Button registerBtn = findViewById(R.id.reg_finish);
    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        doRegistration(usernameBox, emailBox, passBox, confirmPassBox, whatsappBox, phoneBox);
      }
    });

  }

  public void goToUserHomepage(){
    Intent homepage = new Intent(this, Home.class);
    finish();
    startActivity(homepage);
  }

  private void doRegistration(EditText usernameBox, EditText emailBox, EditText passBox, EditText confirmPassBox, EditText whatsappBox, EditText phoneBox) {
    Boolean emailGood = false, passGood = false, phoneGood = false;
    email = emailBox.getText().toString().trim();
    password = passBox.getText().toString().trim();
    passwordConfirmation = confirmPassBox.getText().toString().trim();
    phone = phoneBox.getText().toString().trim();
    //validate email 
    //--------------
    if (email.isEmpty()) {
      emailBox.setError("A valid email is required!");
      emailBox.requestFocus();
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      //If the email does not match the structure of a proper email 
      emailBox.setError("Please provide a valid email address!");
      emailBox.requestFocus();
    } else {
      emailGood = true;
    }

    // validate password
    //------------------
    if (password.isEmpty()) {
      passBox.setError("You will need a password to protect your account!");
      passBox.requestFocus();
    } else if (password.length() < 6) {
      passBox.setError("Your password must be at least 6 characters");
      passBox.requestFocus();
    } else if (passwordConfirmation.isEmpty()) {
      confirmPassBox.setError("Please retype your password here again!");
      confirmPassBox.requestFocus();
    } else if (!password.equals(passwordConfirmation)) {
      confirmPassBox.setError("Your passwords do not match!");
      confirmPassBox.requestFocus();
    } else {
      passGood = true;
    }
    //validate phone number 
    //---------------------

    if (phone.isEmpty()) {
      phoneBox.setError("Please provide a valid phone number that will be verified later");
      phoneBox.requestFocus();
    } else {
      phoneGood = true;
    }

    if (emailGood && passGood && phoneGood) {
      mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            Toast.makeText(Register.this, "Successfully Created Your Account", Toast.LENGTH_SHORT).show();
            FirebaseUser user = mAuth.getCurrentUser();
            Log.d("--->Here::: ", user.toString());
            goToUserHomepage();
          } else {
            Toast.makeText(Register.this, "Oops, something happened! We are working on it!", Toast.LENGTH_SHORT).show();
          }
        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Toast.makeText(Register.this, "Oops! "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });


    }


  }

}
