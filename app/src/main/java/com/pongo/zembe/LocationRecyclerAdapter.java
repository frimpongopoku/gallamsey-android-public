package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.LocationRecyclerViewHolder> {
  Context context;
  ArrayList<GallamseyLocationComponent> locations;

  public LocationRecyclerAdapter(Context context, ArrayList<GallamseyLocationComponent> locations) {
    this.context = context;
    this.locations = locations;
  }

  @NonNull
  @Override
  public LocationRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.recyclable_location_item, parent, false);
    return new LocationRecyclerViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull LocationRecyclerViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 3;
  }

  public class LocationRecyclerViewHolder extends RecyclerView.ViewHolder {
    public LocationRecyclerViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
