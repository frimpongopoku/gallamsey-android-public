package com.pongo.zembe;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserEarningsFragment extends Fragment {
  GroundUser authenticatedUser = new GroundUser();
  RecyclerView recycler;
  TransactionNotificationAdapter adapter;
  ArrayList<Object> transactions = new ArrayList<>();
  GalInterfaceGuru.TrackWalletFragmentState fragmentStateTracker;
  View lastState;
  Context context;

  public UserEarningsFragment(Context context) {
    this.context = context;
    this.fragmentStateTracker = (GalInterfaceGuru.TrackWalletFragmentState) context;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.earnings_fragment, container, false);
    if (lastState != null) {
      return lastState;
    }
    initFragment(v);
    lastState = v;
    return v;
  }

  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }

  public void setOldState(ArrayList<Object> transactions, View view) {
    this.lastState = view;
    this.transactions = transactions;
  }

  public void initFragment(View v) {
    TextView salutation, starCounter, ownerNameDisp, noOfCreatedErrandDisp, noOfErrandsRanDisp, currentBallanceDisp;
    RelativeLayout narratorBox = v.findViewById(R.id.narrator_box);
    salutation = v.findViewById(R.id.salutation);
    adapter = new TransactionNotificationAdapter(getContext(), transactions);
    if (transactions == null || transactions.size() == 0) {
      narratorBox.setVisibility(View.VISIBLE);
      salutation.setText("Hi there, you do not have any transactions yet");
    }

    recycler = v.findViewById(R.id.transactions_recycler);
    starCounter = v.findViewById(R.id.star_count);
    ownerNameDisp = v.findViewById(R.id.owner_name);
    noOfCreatedErrandDisp = v.findViewById(R.id.created_errand_count);
    noOfErrandsRanDisp = v.findViewById(R.id.errand_ran_count);
    currentBallanceDisp = v.findViewById(R.id.current_balance);
//    ---------------------------------------------------------
    currentBallanceDisp.setText(authenticatedUser.getWallet().getCurrentBalance() + Konstants.GH_CURRENCY);
    ownerNameDisp.setText(authenticatedUser.getPreferredName());
    noOfCreatedErrandDisp.setText(String.valueOf(authenticatedUser.getAccolades().getErrandCount()));
    noOfErrandsRanDisp.setText(String.valueOf(authenticatedUser.getAccolades().getGigsCount()));

  }
  public void loadTransactionNotifications(){
    //write here...
  }

  @Override
  public void onDestroy() {
    this.fragmentStateTracker.saveWalletState(transactions, lastState);
    super.onDestroy();
  }
}
