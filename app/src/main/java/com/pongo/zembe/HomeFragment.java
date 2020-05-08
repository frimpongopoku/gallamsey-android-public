package com.pongo.zembe;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

  HashMap<String, ArrayList<String>> items;
  ArrayList<String> test = new ArrayList<>();

  public HomeFragment(  HashMap<String, ArrayList<String>> items) {
    this.items = items;

  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.home_nav_fragment, container, false);
    HomeNewsMultiAdapter adapter = new HomeNewsMultiAdapter(getContext(),items.get("descs"),items.get("profits"), items.get("costs"), items.get("dates"));
    RecyclerView recyclerView = view.findViewById(R.id.home_news_recycler);
    recyclerView.setAdapter(adapter);
    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(manager);
    return view;
  }
}
