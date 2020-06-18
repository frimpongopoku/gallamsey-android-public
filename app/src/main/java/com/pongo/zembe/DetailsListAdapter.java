package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailsListAdapter extends RecyclerView.Adapter<DetailsListAdapter.DetailsViewHolder> {
  Context context;
  String[] list;
  public DetailsListAdapter(Context context,String[] list) {
    this.context = context;
    this.list = list;
  }

  @NonNull
  @Override
  public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View v = inflater.inflate(R.layout.details_item,parent,false);
    return new DetailsViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
    holder.label.setText(list[position]);
  }

  @Override
  public int getItemCount() {
    return list.length;
  }

  public class DetailsViewHolder extends RecyclerView.ViewHolder{
    TextView label;
    public DetailsViewHolder(@NonNull View itemView) {
      super(itemView);
      this.label = itemView.findViewById(R.id.label);
    }
  }
}
