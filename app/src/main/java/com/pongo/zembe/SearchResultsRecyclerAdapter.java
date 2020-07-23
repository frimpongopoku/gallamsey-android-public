package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchResultsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  int ERRAND = 44;
  int RIDER = 33;
  Context context;
  ArrayList<Object> searchRslt;

  public SearchResultsRecyclerAdapter(Context context, ArrayList<Object> searchRslt) {
    this.context = context;
    this.searchRslt = searchRslt;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    if (viewType == ERRAND) {
      View v = inflater.inflate(R.layout.search_item_for_errand, parent, false);
      return new SearchErrandViewHolder(v);
    } else if (viewType == RIDER) {
      View v = inflater.inflate(R.layout.search_item_for_rider, parent, false);
      return new SearchRiderViewHolder(v);
    }
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 6;
  }

  @Override
  public int getItemViewType(int position) {
    if (position % 2 == 0) {
      return ERRAND;
    }
    return RIDER;
  }

  public class SearchErrandViewHolder extends RecyclerView.ViewHolder {
    public SearchErrandViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }

  public class SearchRiderViewHolder extends RecyclerView.ViewHolder {
    public SearchRiderViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
