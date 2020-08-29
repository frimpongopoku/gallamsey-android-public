package com.pongo.zembe;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TasksFragmentGenerator extends Fragment {
  private static String WHICH_FRAGMENT = "FRAGMENT_NAME";
  RelativeLayout narratorBox;
  RecyclerView recyclerView;
  ArrayList<GenericErrandClass> listOfErrands = new ArrayList<>(), listOfGigs = new ArrayList<>();
  View state;
  TextView salutationText;

  public static Fragment newInstance(String whichPage) {
    TasksFragmentGenerator fragment = new TasksFragmentGenerator();
    Bundle args = new Bundle();
    args.putString(WHICH_FRAGMENT, whichPage);
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    String whichPage = getArguments().getString(WHICH_FRAGMENT);

    if (whichPage != null && whichPage.equals(Konstants.TASKS_GIGS_TAB)) {
      View v = inflater.inflate(R.layout.gigs_layout, container, false);
      return initializeGigs(v);

    } else if (whichPage != null && whichPage.equals(Konstants.TASKS_YOUR_ERRANDS_TAB)) {
      View v = inflater.inflate(R.layout.your_created_errands_layout, container, false);
      return initializeYourErrands(v);
    }

    return null;
  }

  private View initializeGigs(View v) {
    salutationText = v.findViewById(R.id.salutation);
    narratorBox = v.findViewById(R.id.narrator_box);
    recyclerView = v.findViewById(R.id.gigs_layout_recycler);
    state = v;
    if (listOfGigs.size() == 0) {
      salutationText.setText("Hi there, there are no records of any jobs you have taken or finished. There are lot of jobs to earn from, check your feed now!");
      narratorBox.setVisibility(View.VISIBLE);
      recyclerView.setVisibility(View.GONE);
    }
    return v;
  }

  private View initializeYourErrands(View v) {
    salutationText = v.findViewById(R.id.salutation);
    narratorBox = v.findViewById(R.id.narrator_box);
    recyclerView = v.findViewById(R.id.your_errands_recycler);
    state = v;
    if (listOfErrands.size() == 0) {
      salutationText.setText("Hi there, you have not created any errands, create now! People are always ready to help.");
      narratorBox.setVisibility(View.VISIBLE);
      recyclerView.setVisibility(View.GONE);
    }
//    inflateErrandsRecycler(new ArrayList<GenericErrandClass>());
    return v;
  }

  private void inflateErrandsRecycler(ArrayList<GenericErrandClass> errands) {
    recyclerView = null;
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    recyclerView = state.findViewById(R.id.your_errands_recycler);
    YourErrandsTabRecyclerAdapter adapter = new YourErrandsTabRecyclerAdapter(getContext(), errands, (YourErrandsTabRecyclerAdapter.YourErrandItemClick) getContext());
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
  }

  private void inflateGigsRecycler(ArrayList<GenericErrandClass> gigs) {
    recyclerView = null;
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    recyclerView = state.findViewById(R.id.your_errands_recycler);
    GigsTabRecyclerAdapter adapter = new GigsTabRecyclerAdapter(getContext(), gigs, (GigsTabRecyclerAdapter.GigItemClick) getContext());
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
  }
}
