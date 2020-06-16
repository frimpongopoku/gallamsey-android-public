package com.pongo.zembe;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChattingAdapter extends RecyclerView.Adapter {

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view;
    if (viewType == Konstants.SENDER_VIEW_TYPE) {
      view = inflater.inflate(R.layout.one_msg_item_sender, parent, false);
      SenderViewHolder holder = new SenderViewHolder(view);
      return holder;
    } else {
      view = inflater.inflate(R.layout.one_msg_item_receipient, parent, false);
      ReceipientViewHolder holder = new ReceipientViewHolder(view);
      return holder;
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (position % 2 == 0) {
      return Konstants.RECEIPIENT_VIEW_TYPE;
    } else {
      return Konstants.SENDER_VIEW_TYPE;
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 9;
  }


  public class SenderViewHolder extends RecyclerView.ViewHolder {
    TextView date, msg;
    public SenderViewHolder(@NonNull View itemView) {
      super(itemView);
      date = itemView.findViewById(R.id.date);
      msg = itemView.findViewById(R.id.msg_content);
    }
  }

  public class ReceipientViewHolder extends RecyclerView.ViewHolder {
    TextView date, msg;
    public ReceipientViewHolder(@NonNull View itemView) {
      super(itemView);
      date = itemView.findViewById(R.id.date);
      msg = itemView.findViewById(R.id.msg_content);
    }
  }
}
