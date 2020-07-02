package com.pongo.zembe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsFragment extends Fragment {

  private GroundUser authenticatedUser;
  GalFirebaseHelper galFirebaseHelper = new GalFirebaseHelper();
  FirebaseFirestore firestore = FirebaseFirestore.getInstance();
  CollectionReference userDB = firestore.collection(Konstants.USER_COLLECTION);
  DocumentReference userDocumentReference;

  public SettingsFragment(GroundUser authUser) {
    this.authenticatedUser = authUser;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    userDocumentReference = userDB.document(authenticatedUser.getUserDocumentID());
    final ArrayList<PaymentContact> phoneNumbersArray = new ArrayList<>();
    final MagicBoxes magicBoxes = new MagicBoxes(getContext());
    View v = inflater.inflate(R.layout.settings_nav_fragment, container, false);
    TextView addMoreLocations = v.findViewById(R.id.add_pickup_location);
    addMoreLocations.setOnClickListener(showAddLocationsPage);
    TextView addPaymentNumber = v.findViewById(R.id.add_payment_mobile_number);
    addPaymentNumber.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        magicBoxes.constructDialogForMobileNumberAddition(phoneNumbersArray, new PhoneNumberDialogContentCallable() {
          @Override
          public void getContent(ArrayList<PaymentContact> phoneNumbers) {

            Log.d("allContactsHere", phoneNumbers.toString());

          }
        }).show();
      }
    });

    return v;
  }

  @Override
  public void onResume() {
    super.onResume();
    galFirebaseHelper.setSnapshotListenerOnDocument(userDocumentReference, new GalInterfaceGuru.SnapshotTakerInterface() {
      @Override
      public void callback(DocumentSnapshot document) {
        if (document.exists()) {
          authenticatedUser = document.toObject(GroundUser.class);
        }
      }
    });
  }

  private View.OnClickListener showAddLocationsPage = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent addlocations = new Intent(getContext(), AddMoreLocations.class);
      addlocations.putExtra("authUser", authenticatedUser);
      startActivity(addlocations);
    }
  };
}
