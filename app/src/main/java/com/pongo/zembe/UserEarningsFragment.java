package com.pongo.zembe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
  TextView salutation, starCounter, ownerNameDisp, noOfCreatedErrandDisp, noOfErrandsRanDisp, currentBallanceDisp;
  private RelativeLayout narratorBox;
  private LinearLayout noAuthBox;
  private Button loginBtn;
  private Activity parentActivity;
  private View.OnClickListener goToLoginPage = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent page = new Intent(parentActivity, Login.class);
      startActivity(page);
      parentActivity.finish();
    }
  };

  public UserEarningsFragment(Context context) {
    this.context = context;
    this.fragmentStateTracker = (GalInterfaceGuru.TrackWalletFragmentState) context;
  }

  private View showNoAuthBox(View v) {
    loginBtn = v.findViewById(R.id.login_btn);
    loginBtn.setOnClickListener(goToLoginPage);
    narratorBox = v.findViewById(R.id.narrator_box);
    salutation = v.findViewById(R.id.salutation);
    narratorBox.setVisibility(View.VISIBLE);
    loginBtn.setVisibility(View.VISIBLE);
    String t = "Hi there, please Sign In to access your wallet";
    salutation.setText(t);
    return v;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    parentActivity = this.getActivity();
    View v = inflater.inflate(R.layout.earnings_fragment, container, false);
    if (lastState != null) {
      return lastState;
    }
    if (authenticatedUser == null) {
      View noAuthView = showNoAuthBox(v);
      this.lastState = noAuthView;
      return noAuthView;
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

    RelativeLayout narratorBox = v.findViewById(R.id.narrator_box);
    salutation = v.findViewById(R.id.salutation);
    adapter = new TransactionNotificationAdapter(getContext(), transactions);
    if (transactions == null || transactions.size() == 0) {
      narratorBox.setVisibility(View.VISIBLE);
      String t = "Hi there, you do not have any transactions yet";
      salutation.setText(t);
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

  public void loadTransactionNotifications() {
    //write here...
  }

  @Override
  public void onDestroy() {
    this.fragmentStateTracker.saveWalletState(transactions, lastState);
    super.onDestroy();
  }
}
