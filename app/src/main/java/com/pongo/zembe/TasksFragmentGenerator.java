package com.pongo.zembe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TasksFragmentGenerator extends Fragment implements GigsTabRecyclerAdapter.GigItemClick, YourErrandsTabRecyclerAdapter.YourErrandItemClick {
  private static String WHICH_FRAGMENT = "FRAGMENT_NAME";

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

    if (whichPage.equals(Konstants.TASKS_GIGS_TAB)) {
      View v = inflater.inflate(R.layout.gigs_layout, container, false);
      return initilizeGigs(v);

    } else if (whichPage.equals(Konstants.TASKS_YOUR_ERRANDS_TAB)) {
      View v = inflater.inflate(R.layout.your_created_errands_layout, container, false);
      return initilizeYourErrands(v);
    }

    return null;
  }

  private View initilizeGigs(View v) {
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    RecyclerView recyclerView = v.findViewById(R.id.gigs_layout_recycler);
    GigsTabRecyclerAdapter adapter = new GigsTabRecyclerAdapter(getContext(), null, this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    return v;
  }

  private View initilizeYourErrands(View v) {
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    RecyclerView recyclerView = v.findViewById(R.id.your_errands_recycler);
    YourErrandsTabRecyclerAdapter adapter = new YourErrandsTabRecyclerAdapter(getContext(), null, this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    return v;
  }

  @Override
  public void onGigItemClick(int position) {
    Toast.makeText(getContext(), "Gig item clicked: " + position, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onYourErrandItemClick(int position) {
    Toast.makeText(getContext(), "Errand item clicked: " + position, Toast.LENGTH_SHORT).show();
  }
}
