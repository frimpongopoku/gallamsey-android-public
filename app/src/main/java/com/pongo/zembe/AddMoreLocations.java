package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class AddMoreLocations extends AppCompatActivity implements LocationItemClicked {

  RecyclerView recyclerView;
  LocationRecyclerAdapter adapter;
  ArrayList<GallamseyLocationComponent> arr  = new ArrayList<>();
  MagicBoxes magicBoxes;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    populate();
    setContentView(R.layout.activity_add_more_locations);
    magicBoxes = new MagicBoxes(this);
    recyclerView = findViewById(R.id.location_list_recycler);
    adapter = new LocationRecyclerAdapter(this,arr, this);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);

  }

  public void populate(){

      for(int i =0; i< 5; i++){
        arr.add(new GallamseyLocationComponent("Location "+i,"32343.43454","6745673456.1"));
      }

  }

  @Override
  public void locationItemCallback(int position) {
    GallamseyLocationComponent location = arr.get(position);
    final int itemPosition = position;
    magicBoxes.constructASimpleDialog("Confirm Location Deletion", "Are you sure you want to delete " + location.getLocationName(), new MagicBoxCallables() {
      @Override
      public void negativeBtnCallable() {
        // do nothing
      }

      @Override
      public void positiveBtnCallable() {
        arr.remove(itemPosition);
        adapter.notifyItemRemoved(itemPosition);
      }
    }).show();

  }
}
