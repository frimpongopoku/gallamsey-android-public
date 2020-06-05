package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileCompletionPage extends AppCompatActivity {
  CoordinatorLayout rootLayout;
  EditText dobBox, usernameBox, whatsappNumberBox;
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  CollectionReference userDB = db.collection(Konstants.USER_COLLECTION);
  FirebaseAuth mAuth = FirebaseAuth.getInstance();
  ProgressBar spinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_completion_page);
    dobBox = findViewById(R.id.prof_dob);
    usernameBox = findViewById(R.id.prof_username);
    whatsappNumberBox = findViewById(R.id.prof_whatsapp_number);
    rootLayout = findViewById(R.id.coordinator_layout);
    spinner = findViewById(R.id.prof_spinner);
  }

  public void saveUserInformation(View v) {
    final String dob, name, number;
    dob = dobBox.getText().toString().trim();
    name = usernameBox.getText().toString().trim();
    number = whatsappNumberBox.getText().toString().trim();
    HashMap<String, Object> dobValidation = RandomHelpersClass.validateDOB(dob);
    FirebaseUser user = mAuth.getCurrentUser();

    if (name.isEmpty()) {
      usernameBox.setError("Sorry, you cant leave this empty");
      usernameBox.requestFocus();
      return;
    }

    if (dob.isEmpty()) {
      dobBox.setError("Please provide a valid date of birth");
      dobBox.requestFocus();
      return;
    }
    if (dobValidation.get("status").equals(false)) {
      String intoOne = RandomHelpersClass.mergeTextsFromArray((ArrayList<String>) dobValidation.get("errors"));
      dobBox.setError(intoOne);
      dobBox.requestFocus();
      return;
    }

    if (number.isEmpty()) {
      whatsappNumberBox.setError("Sorry, you cant leave this empty");
      whatsappNumberBox.requestFocus();
      return;
    }
    // Now, save the updates to firebase
    //----------------------------------

    if (user == null) {
      //This is never going to happen in future, but for now, this check exists
      Snackbar.make(rootLayout, "It looks like you are not currently signed in", Snackbar.LENGTH_SHORT).show();
      return;
    }

    spinner.setVisibility(View.VISIBLE);
    userDB
      .whereEqualTo("uniqueID", user.getUid())
      .get()
      .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot documents) {
          //this will actually only bring one item every time
          //-------------------------------------------------
          GroundUser recoveredUser;
          for (QueryDocumentSnapshot document : documents) {
            if (document.exists()) {
              recoveredUser = document.toObject(GroundUser.class);
              String documentID = document.getReference().getId();
              recoveredUser.setWhatsappNumber(number);
              recoveredUser.setDOB(dob);
              recoveredUser.setPreferredName(name);
              //reset old content with the newly updated version
              //------------------------------------------------
              userDB
                .document(documentID).set(recoveredUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                    spinner.setVisibility(View.INVISIBLE);
                    ProfileCompletionDialog dialog = new ProfileCompletionDialog();
                    dialog.show(getSupportFragmentManager(), "Profile Completion Congratulatory Message");
                  }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  Log.w("profileUpdateError:", e.getMessage());
                  Snackbar.make(rootLayout, "For some reason, we could not update your profile. Please contact us if this error persists.", Snackbar.LENGTH_SHORT).show();
                }
              });
            } else {
              Snackbar.make(rootLayout, "This is weird, we could not find your profile", Snackbar.LENGTH_SHORT).show();
            }
          }
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.w("profileCompletionPage::", e.getMessage());
          Snackbar.make(rootLayout, "Oops something happend, please try again later", Snackbar.LENGTH_SHORT).show();
        }
      });
  }


  public void goToMain(View v) {
    Intent main = new Intent(this, MainActivity.class);
    startActivity(main);

  }
}
