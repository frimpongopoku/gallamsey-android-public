package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailsListAdapter extends RecyclerView.Adapter<DetailsListAdapter.DetailsViewHolder> {
  Context context;
  ArrayList<String> list;
  OnDetailItemsClick detailItemsClick;
  public DetailsListAdapter(Context context, ArrayList<String> list, OnDetailItemsClick detailItemsClick) {
    this.context = context;
    this.list = list;
    this.detailItemsClick = detailItemsClick;
  }

  @NonNull
  @Override
  public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View v = inflater.inflate(R.layout.details_item,parent,false);
    return new DetailsViewHolder(v,detailItemsClick);
  }

  @Override
  public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
    holder.label.setText(list.get(position));
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public class DetailsViewHolder extends RecyclerView.ViewHolder {
    TextView label;
    OnDetailItemsClick detailItemsClick;
    public DetailsViewHolder(@NonNull View itemView, final OnDetailItemsClick detailItemsClick) {
      super(itemView);
      this.label = itemView.findViewById(R.id.label);
      this.detailItemsClick = detailItemsClick;
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          detailItemsClick.onDetailItemClick(getAdapterPosition());
        }
      });
    }
  }
}

interface OnDetailItemsClick {
  void onDetailItemClick(int pos);
}
