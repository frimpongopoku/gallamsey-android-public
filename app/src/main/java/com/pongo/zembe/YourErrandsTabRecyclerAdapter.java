package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pongo.zembe.GigsTabRecyclerAdapter;

import java.util.ArrayList;

public class YourErrandsTabRecyclerAdapter extends RecyclerView.Adapter<YourErrandsTabRecyclerAdapter.YourErrandsTabRecyclerViewHolder> {
  Context context;
  ArrayList<Object> listOfErrands = new ArrayList<>();
  YourErrandItemClick listener;

  public YourErrandsTabRecyclerAdapter(Context context, ArrayList<Object> listOfErrands, YourErrandItemClick listener) {
    this.context = context;
    this.listOfErrands = listOfErrands;
    this.listener = listener;
  }


  @NonNull
  @Override
  public YourErrandsTabRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.created_errands_recyclable, parent, false);
    return new YourErrandsTabRecyclerViewHolder(v, this.listener);
  }

  @Override
  public void onBindViewHolder(@NonNull YourErrandsTabRecyclerViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 4;
  }

  public class YourErrandsTabRecyclerViewHolder extends RecyclerView.ViewHolder {
    YourErrandItemClick listener;
    RelativeLayout container;

    public YourErrandsTabRecyclerViewHolder(@NonNull View itemView, final YourErrandItemClick listener) {
      super(itemView);
      this.listener = listener;
      this.container = itemView.findViewById(R.id.main_container);
      container.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          listener.onYourErrandItemClick(getAdapterPosition());
        }
      });
    }
  }

  public interface YourErrandItemClick {
    void onYourErrandItemClick(int position);
  }
}
