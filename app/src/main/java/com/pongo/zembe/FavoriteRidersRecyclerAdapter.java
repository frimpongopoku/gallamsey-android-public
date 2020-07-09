package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteRidersRecyclerAdapter extends  RecyclerView.Adapter<FavoriteRidersRecyclerAdapter.FavoriteRidersRecyclerViewHolder> {

  Context context;
  ArrayList<SimpleUser> riders;
  RidersItemClick listener;

  public FavoriteRidersRecyclerAdapter(Context context, ArrayList<SimpleUser> riders, RidersItemClick onClickListener) {
    this.context = context;
    this.riders = riders;
    this.listener = onClickListener;
  }

  @NonNull
  @Override
  public FavoriteRidersRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.favorite_ryders_recyclable_item,parent,false);
    return new FavoriteRidersRecyclerViewHolder(v,listener);
  }

  @Override
  public void onBindViewHolder(@NonNull FavoriteRidersRecyclerViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 5;
  }

  public class FavoriteRidersRecyclerViewHolder extends RecyclerView.ViewHolder{
    RelativeLayout container;
    RidersItemClick listener;
    public FavoriteRidersRecyclerViewHolder(@NonNull View itemView, final RidersItemClick listener) {
      super(itemView);
      this.listener = listener;
      container = itemView.findViewById(R.id.favorites_container);
      container.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          listener.onRiderClick(getAdapterPosition());
        }
      });
    }
  }

  public interface  RidersItemClick {
    void onRiderClick(int position);
  }
}
