package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  Context context;
  ArrayList<Object> transactions;

  public TransactionNotificationAdapter(Context context, ArrayList<Object> transactions) {
    this.context = context;
    this.transactions = transactions;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v;
    LayoutInflater inflater = LayoutInflater.from(context);
    if (viewType == Konstants.TEXT_PAYMENT_NOTIFICATION) {
      v = inflater.inflate(R.layout.notfication_card_type_1, parent, false);
      return new TextNotificationViewHolder(v);
    } else if (viewType == Konstants.IMAGE_PAYMENT_NOTIFICATION) {
      v = inflater.inflate(R.layout.notfication_card_type_2, parent, false);
      return new ImageNotificationViewHolder(v);
    }
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

  }

  @Override
  public int getItemViewType(int position) {
    if (position % 2 == 0) {
      return Konstants.TEXT_PAYMENT_NOTIFICATION;
    }
    return Konstants.IMAGE_PAYMENT_NOTIFICATION;
  }

  @Override
  public int getItemCount() {
    return 0;
  }


  public class TextNotificationViewHolder extends RecyclerView.ViewHolder {
    public TextNotificationViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }

  public class ImageNotificationViewHolder extends RecyclerView.ViewHolder {
    public ImageNotificationViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
