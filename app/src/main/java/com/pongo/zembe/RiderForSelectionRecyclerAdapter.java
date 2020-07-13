package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RiderForSelectionRecyclerAdapter extends RecyclerView.Adapter<RiderForSelectionRecyclerAdapter.RiderForSelectionViewHolder> {


  Context context;
  ArrayList<Object> riders;
  SelectRiderCallback listener;

  public RiderForSelectionRecyclerAdapter(Context context, ArrayList<Object> riders, SelectRiderCallback listener) {
    this.context = context;
    this.riders = riders;
    this.listener = listener;
  }

  @NonNull
  @Override
  public RiderForSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.rider_for_selection_recyclable, parent, false);
    return new RiderForSelectionViewHolder(v, listener);
  }

  @Override
  public void onBindViewHolder(@NonNull RiderForSelectionViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 6;
  }

  public class RiderForSelectionViewHolder extends RecyclerView.ViewHolder {
    LinearLayout riderCard;
    SelectRiderCallback onClickListener;

    public RiderForSelectionViewHolder(@NonNull View itemView, SelectRiderCallback listener) {
      super(itemView);
      onClickListener = listener;
      riderCard = itemView.findViewById(R.id.rider_card_item);
      riderCard.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          onClickListener.onRiderClick(getAdapterPosition());
        }
      });
    }
  }

  interface SelectRiderCallback {
    void onRiderClick(int position);
  }
}
