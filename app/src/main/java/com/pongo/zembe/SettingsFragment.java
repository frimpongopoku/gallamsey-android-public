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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsFragment extends Fragment {

  private GroundUser authenticatedUser;

  public SettingsFragment(GroundUser authUser){
    this.authenticatedUser = authUser;
  }
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

  private View.OnClickListener showAddLocationsPage = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent addlocations = new Intent(getContext(), AddMoreLocations.class);
      addlocations.putExtra("authUser",authenticatedUser);
      startActivity(addlocations);
    }
  };
}
