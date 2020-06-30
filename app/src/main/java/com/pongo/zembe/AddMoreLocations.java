package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class AddMoreLocations extends AppCompatActivity {

  RecyclerView recyclerView;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_more_locations);
    recyclerView = findViewById(R.id.location_list_recycler);
    LocationRecyclerAdapter adapter = new LocationRecyclerAdapter(this,null);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);

  }
}
