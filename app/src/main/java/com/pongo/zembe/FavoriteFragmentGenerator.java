package com.pongo.zembe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteFragmentGenerator extends Fragment {


  private static String WHICH_FRAGMENT = "FRAGMENT_NAME";

  public static FavoriteFragmentGenerator newInstance(String whichFragment) {
    FavoriteFragmentGenerator fragment = new FavoriteFragmentGenerator();
    Bundle args = new Bundle();
    args.putString(WHICH_FRAGMENT, whichFragment);
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    String whichFragment = null;
    if (getArguments() != null) {
      whichFragment = getArguments().getString(WHICH_FRAGMENT);
    }
    if (whichFragment.equals(Konstants.USER_TEMPLATES_TAB)) {
      View v = LayoutInflater.from(getContext()).inflate(R.layout.errand_templates_fragment, container, false);

      return initializeTemplatesTab(v);
    } else if (whichFragment.equals(Konstants.FAVORITE_RIDERS_TAB)) {

      return LayoutInflater.from(getContext()).inflate(R.layout.favorite_riders_fragment, container, false);
    }
    return null;
  }

  private View initializeTemplatesTab(View v) {
    RecyclerView recycler = v.findViewById(R.id.templates_recycler);
    TempatesRecyclerAdapter adapter = new TempatesRecyclerAdapter(getContext(), new ArrayList<Errand>(),(TempatesRecyclerAdapter.TemplateItemClick) getContext() );
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    recycler.setLayoutManager(manager);
    recycler.setAdapter(adapter);
    return v;
  }
}
