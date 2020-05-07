package com.pongo.zembe;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeNewsMultiAdapter extends RecyclerView.Adapter {
  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public int getItemViewType(int position) {
    return super.getItemViewType(position);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  //........ TextCard

  class TextCard extends RecyclerView.ViewHolder{
    public TextCard(@NonNull View itemView) {
      super(itemView);
    }
  }


  //......... ImageCard
  class ImageCard extends RecyclerView.ViewHolder{
    public ImageCard(@NonNull View itemView) {
      super(itemView);
    }
  }
}
