package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

  EditText emailBox, passwordBox;
  FirebaseAuth mAuth;
  String email, password;
  private GoogleSignInClient mGoogleSignInClient;
  private static String TAG = "LOGIN-AREA::->";
  ProgressBar spinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    spinner = findViewById(R.id.log_spinner);
    spinner.setVisibility(View.INVISIBLE);
    mAuth = FirebaseAuth.getInstance();
    emailBox = findViewById(R.id.log_email);
    passwordBox = findViewById(R.id.log_password);
    Button loginButton = findViewById(R.id.login_btn);
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        doLogin(emailBox, passwordBox);
      }
    });
    //Google sign in step 1
    setGoogleDialogUp();
  }


  @Override
  protected void onStart() {
    super.onStart();
    if (mAuth.getCurrentUser() != null) {
      goToUserHomepage();
    }
  }

  //Google sign in step 2
  private void setGoogleDialogUp() {
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .build();
    //attach google sign in options to Google Dialog
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
  }

  //Google Sign in step 3
  public void loginWithGoogle(View v) {
    spinner.setVisibility(View.VISIBLE);
    Intent withGoogle = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(withGoogle, Konstants.GOOGLE_SIGN_IN_CODE);
  }

  // CHECKING FOR GOOGLE ACCOUNT DIALOG RESULTS
  //-------------------------------------------
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //Google Sign in step 4
    if (requestCode == Konstants.GOOGLE_SIGN_IN_CODE) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account.getIdToken());

      } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "Oops! Failed to signin with google!" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
      }

    }
  }

  //AUTHENTICATE USER's CHOSEN GOOGLE ACCOUNT WITH APP FIREBASE
  //-----------------------------------------------------------
  //Google Sign in step 5
  private void firebaseAuthWithGoogle(String token) {
    AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
    mAuth.signInWithCredential(credential)
      .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          spinner.setVisibility(View.INVISIBLE);
          if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            FirebaseUser user = mAuth.getCurrentUser();
            Toast.makeText(Login.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
            goToUserHomepage();

          } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithCredential:failure", task.getException());
            Toast.makeText(Login.this, "Oops, couldnt sign in you in! " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

          }
        }
      });
  }

  public void goToUserHomepage() {
    Intent homepage = new Intent(this, Home.class);
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
    if (password.isEmpty()) {
      passwordBox.setError("Please provide a valid password to your account");
      passwordBox.requestFocus();
    } else {
      passwordGood = true;
    }

    if (emailGood && passwordGood) {
      mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            Toast.makeText(Login.this, "Nice, good to go!", Toast.LENGTH_SHORT).show();
            goToUserHomepage();
          } else {
            Toast.makeText(Login.this, "Oops!" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
