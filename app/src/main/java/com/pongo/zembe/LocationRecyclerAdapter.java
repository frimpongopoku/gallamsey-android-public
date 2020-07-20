package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.LocationRecyclerViewHolder> {
  Context context;
  ArrayList<GallamseyLocationComponent> locations;
  LocationItemClicked itemClickedCallback ;
  private GroundUser authenticatedUser;

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
    GallamseyLocationComponent location = locations.get(position);
    if(location.getLocationName().equals(authenticatedUser.getGeoLocation().getLocationName())){

      holder.locationIcon.setImageDrawable(context.getDrawable(R.drawable.location_home));
      holder.locationName.setText(locations.get(position).getLocationName()+ " - Primary Location");
    }
    else{
      holder.locationName.setText(locations.get(position).getLocationName());
    }


  }

  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }

  @Override
  public int getItemCount() {
    return locations.size();
  }

  public class LocationRecyclerViewHolder extends RecyclerView.ViewHolder {
    ImageView locationIcon;
    TextView locationName;
    LocationItemClicked locationItemClicked;
    public LocationRecyclerViewHolder(@NonNull View itemView, final LocationItemClicked locationItemClicked) {
      super(itemView);
      this.locationItemClicked = locationItemClicked;
      this.locationName = itemView.findViewById(R.id.location_list_item_name);
      this.locationIcon = itemView.findViewById(R.id.location_icon);

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
