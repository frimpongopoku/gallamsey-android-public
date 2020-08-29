package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GigsTabRecyclerAdapter extends RecyclerView.Adapter<GigsTabRecyclerAdapter.GigsTabReyclerViewHolder> {

  Context context;
  ArrayList<GenericErrandClass> listOfErrands = new ArrayList<>();
  GigItemClick listener;

  public GigsTabRecyclerAdapter(Context context, ArrayList<GenericErrandClass> listOfErrands, GigItemClick listener) {
    this.context = context;
    this.listOfErrands = listOfErrands;
    this.listener = listener;
  }

  @NonNull
  @Override
  public GigsTabReyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.gigs_recyclable_item,parent,false);
    return new GigsTabReyclerViewHolder(v,this.listener);
  }

  @Override
  public void onBindViewHolder(@NonNull GigsTabReyclerViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return listOfErrands.size();
  }

  public class GigsTabReyclerViewHolder extends RecyclerView.ViewHolder {
    GigItemClick listener;
    RelativeLayout container;
    public GigsTabReyclerViewHolder(@NonNull View itemView, final GigItemClick listener) {
      super(itemView);
      this.listener = listener;
      this.container = itemView.findViewById(R.id.main_container);
      container.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          listener.onGigItemClick(getAdapterPosition());
        }
      });
    }
  }

  public interface GigItemClick {
    void onGigItemClick(int position);
  }
}
