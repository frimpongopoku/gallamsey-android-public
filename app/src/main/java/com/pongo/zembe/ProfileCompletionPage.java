package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileCompletionPage extends AppCompatActivity {
  CoordinatorLayout rootLayout;
  EditText dobBox, usernameBox, whatsappNumberBox;
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  CollectionReference userDB;
  FirebaseAuth mAuth = FirebaseAuth.getInstance();
  FirebaseUser user;
  ProgressBar spinner;
  QuerySnapshot foundUserDocs;
  LinearLayout elementsDiv;
  LinearLayout profilePicture;
  ImageUploadHelper imageUploadHelper;
  byte[] selectedImageBytes = null;
  CircleImageView imageHolder;
  StorageReference storageReference;
  String selectedImageExt;
  User recoveredUser; //Gallamsey User class
  ImageView roundCloseBtn;
  Activity activity;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_completion_page);
    activity = this;
    user = mAuth.getCurrentUser();
    spinner = findViewById(R.id.prof_spinner);
    //-----------------

//    if (user == null) {
//      goToLogin();
//    } else {
//      userDB = db.collection(Konstants.USER_COLLECTION);
//      spinner.setVisibility(View.VISIBLE);
//      retrieveUserOldInfo(user);
//    }
    //--------------------------------------------------
    roundCloseBtn = findViewById(R.id.x_button);
    storageReference = FirebaseStorage.getInstance().getReference(Konstants.PROFILE_PICTURES_COLLECTION);
    dobBox = findViewById(R.id.prof_dob);
    usernameBox = findViewById(R.id.prof_username);
    whatsappNumberBox = findViewById(R.id.prof_whatsapp_number);
    rootLayout = findViewById(R.id.coordinator_layout);
    elementsDiv = findViewById(R.id.prof_mother_div);
//    elementsDiv.setVisibility(View.GONE);
    profilePicture = findViewById(R.id.profile_picture);
    imageHolder = findViewById(R.id.image_holder);
    imageUploadHelper = new ImageUploadHelper(this);
    final MagicBoxes dialogBoxCreator = new MagicBoxes(this);

    //--------------------------------------------------
    profilePicture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startImageChooser();
      }
    });
    imageHolder.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startImageChooser();
      }
    });
    roundCloseBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Dialog dialog = dialogBoxCreator.constructASimpleDialog("Confirm Delete", "Your picture will be removed and reset to the default picture, until you upload another one", new MagicBoxCallables() {
          @Override
          public void negativeBtnCallable() {

          }

          @Override
          public void positiveBtnCallable() {
            deleteCurrentProfilePicture();
          }
        });
        dialog.show();
      }
    });
  }


  public void deleteCurrentProfilePicture() {
    final String URL = recoveredUser.getProfilePictureURL();
    recoveredUser.setProfilePictureURL(null);
    userDB.document(recoveredUser.getUserDocumentID()).set(recoveredUser)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          roundCloseBtn.setVisibility(View.GONE);
          imageHolder.setVisibility(View.GONE);
          getProfilePictureOnLoad(recoveredUser);
          // now really delete from firebase storage
          StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(URL);
          photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              Toast.makeText(ProfileCompletionPage.this, "Your picture has been removed", Toast.LENGTH_SHORT).show();
            }
          }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Toast.makeText(ProfileCompletionPage.this, "We could not remove your picture:: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          });
        }
      });
  }

  private void startImageChooser() {
    imageUploadHelper.openFileChooserWithCropper(activity, 4, 3);
  }

  private void goToLogin() {
    Intent login = new Intent(this, Login.class);
    startActivity(login);
    finish();
  }


  public void retrieveUserOldInfo(FirebaseUser user) {
    userDB.whereEqualTo("uniqueID", user.getUid()).get()
      .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot documents) {
          //this will actually only bring one item every time
          //-------------------------------------------------
          foundUserDocs = documents;
          for (QueryDocumentSnapshot document : documents) {
            if (document.exists()) {
              GroundUser gUser = document.toObject(GroundUser.class);
              gUser.setUserDocumentID(document.getReference().getId());
              //set global user variable
              recoveredUser = gUser;
              dobBox.setText(gUser.getDob());
              usernameBox.setText(gUser.getPreferredName());
              whatsappNumberBox.setText(gUser.getWhatsappNumber());
              spinner.setVisibility(View.INVISIBLE);
              elementsDiv.setVisibility(View.VISIBLE);
              getProfilePictureOnLoad(gUser);
            }
          }
        }
      }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Toast.makeText(ProfileCompletionPage.this, "For some reason, we could not update your profile. Please contact us if this error persists.", Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void getProfilePictureOnLoad(User gallamseyUser) {

    if (gallamseyUser.getProfilePictureURL() != null && !gallamseyUser.getProfilePictureURL().equals(Konstants.EMPTY)) {
      //------if the user has profile picture, show that one instead of the default profile photos
      Picasso.get().load(gallamseyUser.getProfilePictureURL()).into(imageHolder);
      profilePicture.setVisibility(View.GONE);
      imageHolder.setVisibility(View.VISIBLE);
      roundCloseBtn.setVisibility(View.VISIBLE);
    } else {
      profilePicture.setVisibility(View.VISIBLE);
      // ---- it means the user still hasn't uploaded a profile picture yet
      // ----- display a default picture depending on their gender
      // ----- by default, a female avatar is shown
      if (gallamseyUser.getGender().equals(Konstants.MALE)) {
        profilePicture.setBackgroundResource(R.drawable.profile_dummy_box_male);
      } else if (gallamseyUser.getGender().equals(Konstants.OTHER)) {
        profilePicture.setBackgroundResource(R.drawable.profile_dummy_box_other);
      } else {
        profilePicture.setBackgroundResource(R.drawable.profile_dummy_box_female);
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    imageUploadHelper.collectCroppedImage(requestCode, resultCode, data, new ImageUploadHelper.CroppingImageCallback() {
      @Override
      public void getCroppedImage(Uri uri) {
        selectedImageExt = imageUploadHelper.getFileExtension(uri);
        imageUploadHelper.compressImageToBytes(uri, new ImageUploadHelper.CompressedImageToBytesCallback() {
          @Override
          public void getCompressedImage(byte[] compressedImage) {
            selectedImageBytes = compressedImage;
            profilePicture.setVisibility(View.GONE);
            imageHolder.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(compressedImage, 0, compressedImage.length);
            imageHolder.setImageBitmap(bitmap);

          }
        });

      }

      @Override
      public void getCroppingError(Exception e) {
        e.printStackTrace();
        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

      }
    });
  }

  public void uploadImage(final ImageUploadCallback imageUploadCallback) {
    String filename = user.getUid() + "_profile_" + System.currentTimeMillis() + "." + selectedImageExt;
    final StorageReference fileReference = storageReference.child(filename);
    fileReference.putBytes(selectedImageBytes)
      .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
          fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
              imageUploadCallback.getDownloadableURL(uri.toString());
            }
          });

        }
      }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Toast.makeText(ProfileCompletionPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void saveUserInformation(View v) {
    final String dob, name, number;
    dob = dobBox.getText().toString().trim();
    name = usernameBox.getText().toString().trim();
    number = whatsappNumberBox.getText().toString().trim();
    HashMap<String, Object> dobValidation = MyHelper.validateDOB(dob);
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
      String intoOne = MyHelper.mergeTextsFromArray((ArrayList<String>) dobValidation.get("errors"));
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
    for (QueryDocumentSnapshot document : foundUserDocs) {
      if (document.exists()) {
        recoveredUser = document.toObject(GroundUser.class);
        final String documentID = document.getReference().getId();
        recoveredUser.setWhatsappNumber(number);
        recoveredUser.setDob(dob);
        recoveredUser.setPreferredName(name);
        //reset old content with the newly updated version
        //------------------------------------------------

        // Upload Profile Picture if available, else : do normal user updates
        //------------------------------------
        if (selectedImageBytes != null) {
          uploadImage(new ImageUploadCallback() {
            @Override
            public void getDownloadableURL(String URL) {
              recoveredUser.setProfilePictureURL(URL);
              actuallyUpdateUserInfo(recoveredUser, documentID);
              roundCloseBtn.setVisibility(View.VISIBLE);
            }
          });
        } else {
          actuallyUpdateUserInfo(recoveredUser, documentID);
        }

      } else {
        Snackbar.make(rootLayout, "This is weird, we could not find your profile", Snackbar.LENGTH_SHORT).show();
      }
    }

  }

  public void actuallyUpdateUserInfo(User gallamseyUser, String documentID) {
    userDB
      .document(documentID).set(gallamseyUser)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          spinner.setVisibility(View.INVISIBLE);
          ProfileCompletionDialog dialog = new ProfileCompletionDialog();
          dialog.show(getSupportFragmentManager(), "Profile Update Congratulatory Message");
        }
      }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.w("profileUpdateError:", e.getMessage());
        Snackbar.make(rootLayout, "For some reason, we could not update your profile. Please contact us if this error persists.", Snackbar.LENGTH_SHORT).show();
      }
    });

  }


  public void goToMain(View v) {
    Intent main = new Intent(this, MainActivity.class);
    startActivity(main);

  }

  public void goToHomepage(View v) {
    Intent home = new Intent(this, Home.class);
    startActivity(home);

  }

  public interface ImageUploadCallback {
    void getDownloadableURL(String URL);
  }
}
