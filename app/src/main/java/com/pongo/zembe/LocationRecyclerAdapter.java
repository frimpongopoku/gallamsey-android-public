package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.LocationRecyclerViewHolder> {
  Context context;
  ArrayList<GallamseyLocationComponent> locations;
  LocationItemClicked itemClickedCallback ;

  public LocationRecyclerAdapter(Context context, ArrayList<GallamseyLocationComponent> locations, LocationItemClicked itemClickedCallback) {
    this.context = context;
    this.locations = locations;
    this.itemClickedCallback = itemClickedCallback;
  }

  @NonNull
  @Override
  public LocationRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.recyclable_location_item, parent, false);
    return new LocationRecyclerViewHolder(v, itemClickedCallback);
  }

  @Override
  public void onBindViewHolder(@NonNull LocationRecyclerViewHolder holder, int position) {
    holder.locationName.setText(locations.get(position).getLocationName());
  }

  @Override
  public int getItemCount() {
    return locations.size();
  }

  public class LocationRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView locationName;
    LocationItemClicked locationItemClicked;
    public LocationRecyclerViewHolder(@NonNull View itemView, final LocationItemClicked locationItemClicked) {
      super(itemView);
      this.locationItemClicked = locationItemClicked;
      this.locationName = itemView.findViewById(R.id.location_list_item_name);

      locationName.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          locationItemClicked.locationItemCallback(getAdapterPosition());
        }
      });
    }
  }
}
interface LocationItemClicked{
  void locationItemCallback(int position);
}
