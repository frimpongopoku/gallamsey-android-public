package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

  private static String TAG = "REGISTRATION-AREA::->";
  String username, email, password, passwordConfirmation, whatsappNumber, phone, dob, gender = Konstants.OTHER;
  EditText usernameBox, emailBox, passBox, confirmPassBox, whatsappBox, phoneBox, dobBox;
  Spinner genderDropDown;
  ProgressBar spinner;
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  Boolean infoProvided;
  GroundUser newUser;
  private GoogleSignInClient mGoogleSignInClient;
  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    mAuth = FirebaseAuth.getInstance();
    usernameBox = findViewById(R.id.reg_username);
    emailBox = findViewById(R.id.reg_email);
    passBox = findViewById(R.id.reg_password);
    confirmPassBox = findViewById(R.id.reg_confirm_password);
    whatsappBox = findViewById(R.id.reg_whatsapp_number);
    phoneBox = findViewById(R.id.reg_phone);
    spinner = findViewById(R.id.reg_spinner);
    dobBox = findViewById(R.id.reg_dob);

//    -------- DROP DOWN ----------------
    genderDropDown = findViewById(R.id.gender_dropdown);
    ArrayAdapter<CharSequence> genderDropdownAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
    genderDropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    genderDropDown.setAdapter(genderDropdownAdapter);

    genderDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gender = adapterView.getItemAtPosition(i).toString();
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });

//    -----------------------------------
    Button registerBtn = findViewById(R.id.reg_finish);
    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        doRegistration();
      }
    });
    setGoogleDialogUp();
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (mAuth.getCurrentUser() != null) {
      goToUserHomepage();
    }
  }


  private void setGoogleDialogUp() {
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .build();
    //attach google sign in options to Google Dialog
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
  }

  public void registerWithGoogle(View v) {
    spinner.setVisibility(View.VISIBLE);
    Intent withGoogle = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(withGoogle, Konstants.GOOGLE_SIGN_UP_CODE);
  }

  public void goToProfileCompletion() {
    Intent completeProfilePage = new Intent(this, ProfileCompletionPage.class);
    finish();
    startActivity(completeProfilePage);
  }

  public void goToUserHomepage() {
    Intent homepage = new Intent(this, Home.class);
    homepage.putExtra(Konstants.AUTH_USER_KEY, newUser);
    finish();
    startActivity(homepage);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //Google Sign in step 4
    if (requestCode == Konstants.GOOGLE_SIGN_UP_CODE) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account.getIdToken());

      } catch (Exception e) {
        spinner.setVisibility(View.INVISIBLE);
        e.printStackTrace();
        Toast.makeText(this, "Oops! Failed to sign up with google! " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
      }

    }
  }

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
            saveOtherProfileInfo(user, Konstants.GOOGLE_AUTH_TYPE);
          } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signUpWithCredential:failure", task.getException());
            Toast.makeText(Register.this, "Oops, couldn't sign you up with google! " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
          }
        }
      });
  }

  private boolean hasUserProvidedOtherInfo() {
    String preferredName = usernameBox.getText().toString();
    String whatsappPhone = whatsappBox.getText().toString();
    String DOB = dobBox.getText().toString();
    return !preferredName.isEmpty() && !whatsappPhone.isEmpty() && !whatsappPhone.isEmpty() && !DOB.isEmpty();

  }

  private void saveOtherProfileInfo(FirebaseUser user, int authType) {
    infoProvided = false;
    String preferredName = usernameBox.getText().toString();
    String whatsappPhone = whatsappBox.getText().toString();
    //if the authentication type is by google, dont get email phone from textbox, get it from googleUserReturned
    String phone = authType == Konstants.GOOGLE_AUTH_TYPE ? String.valueOf(user.getPhoneNumber()) : phoneBox.getText().toString();
    String email = authType == Konstants.GOOGLE_AUTH_TYPE ? user.getEmail() : emailBox.getText().toString();
    String DOB = dobBox.getText().toString();

    infoProvided = hasUserProvidedOtherInfo() && !phone.isEmpty();

//    if (!preferredName.isEmpty() && !phone.isEmpty() && !DOB.isEmpty() && !whatsappPhone.isEmpty()) {
//      infoProvided = true;
//    }
    //--------------------------------------------------------------------------------
    //Every user starts as a ground user -- upgrade later
    //--------------------------------------------------------------------------------
    newUser = new GroundUser(preferredName, DOB, email, phone, whatsappPhone, user.getUid(), Konstants.GROUND_USER, gender);
    cleanUp();
    if (!infoProvided) {
      goToProfileCompletion();
    } else {
      goToUserHomepage();
    }

    db.collection(Konstants.USER_COLLECTION)
      .document()
      .set(newUser)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          cleanUp();
          if (!infoProvided) {
            goToProfileCompletion();
          } else {
            goToUserHomepage();
          }
        }
      }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "DatabaseError: " + e.getMessage());
        Toast.makeText(Register.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
      }
    });
    if (!infoProvided) {
      Toast.makeText(this, "You are good to go. You can provide all missing data later on.", Toast.LENGTH_LONG).show();
    }
  }


  public void cleanUp() {
    emailBox.setText("");
    usernameBox.setText("");
    phoneBox.setText("");
    passBox.setText("");
    confirmPassBox.setText("");
    whatsappBox.setText("");
    dobBox.setText("");
  }

  private boolean validatePassword() {
    password = MyHelper.grabCleanText(passBox);
    if (password.isEmpty()) {
      passBox.setError("You will need a password to protect your account!");
      passBox.requestFocus();
      return false;
    } else if (password.length() < 6) {
      passBox.setError("Your password must be at least 6 characters");
      passBox.requestFocus();
      return false;
    } else if (passwordConfirmation.isEmpty()) {
      confirmPassBox.setError("Please retype your password here again!");
      confirmPassBox.requestFocus();
      return false;
    } else if (!password.equals(passwordConfirmation)) {
      confirmPassBox.setError("Your passwords do not match!");
      confirmPassBox.requestFocus();
      return false;
    }

    return true;
  }

  private boolean validateEmail() {
    email = MyHelper.grabCleanText(emailBox);
    if (email.isEmpty()) {
      emailBox.setError("A valid email is required!");
      emailBox.requestFocus();
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      //If the email does not match the structure of a proper email
      emailBox.setError("Please provide a valid email address!");
      emailBox.requestFocus();
    }

    return true;

  }


  private boolean validatePhone() {
    if (phone.isEmpty()) {
      phoneBox.setError("Please provide a valid phone number that will be verified later");
      phoneBox.requestFocus();
      return false;
    }

    return true;
  }

  private void doRegistration() {
    Boolean emailGood = false, passGood = false, phoneGood = false;
    email = MyHelper.grabCleanText(emailBox);
    password = MyHelper.grabCleanText(passBox);
    passwordConfirmation = MyHelper.grabCleanText(confirmPassBox);
    phone = MyHelper.grabCleanText(phoneBox);
    dob = dobBox.getText().toString().trim();

    //validate date of birth (DOB) only if the user typed something
    //-----------------------------
    if (!dob.isEmpty() && MyHelper.validateDOB(dob).get("status").equals(false)) {
      ArrayList<String> dobErrors = new ArrayList<>();
      Object obj = MyHelper.validateDOB(dob).get("errors");
      if (obj instanceof ArrayList) {
        dobErrors = (ArrayList<String>) obj;
      }
      dobBox.setError(MyHelper.mergeTextsFromArray(dobErrors));
      dobBox.requestFocus();
      return;
    }
    if (!validateEmail()) return;
    if (!validatePassword()) return;
    if (!validatePhone()) return;

    spinner.setVisibility(View.VISIBLE);
    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
          Toast.makeText(Register.this, "Successfully Created Your Account", Toast.LENGTH_SHORT).show();
          FirebaseUser user = mAuth.getCurrentUser();
          saveOtherProfileInfo(user, Konstants.EMAIL_AND_PASSWORD_AUTH_TYPE);
        } else {
          spinner.setVisibility(View.INVISIBLE);
          Log.w(TAG, task.getException().getMessage());
          Toast.makeText(Register.this, "Oops, something happened! " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        spinner.setVisibility(View.INVISIBLE);
        Log.w(TAG, "withEmailAndPasswordException:" + e.getMessage());
        Toast.makeText(Register.this, "Oops! " + e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });

  }

}
