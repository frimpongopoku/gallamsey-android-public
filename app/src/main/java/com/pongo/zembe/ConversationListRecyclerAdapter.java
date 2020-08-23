package com.pongo.zembe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationListRecyclerAdapter extends RecyclerView.Adapter<ConversationListRecyclerAdapter.ConversationListViewHolder> {
  private Context context;
  private ArrayList<ConversationWithNotificationItem> chats = new ArrayList<>();

  public ConversationListRecyclerAdapter(Context context, ArrayList<ConversationWithNotificationItem> chats) {
    this.context = context;
    this.chats = chats;
  }

  @NonNull
  @Override
  public ConversationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View v = inflater.inflate(R.layout.chat_list_item, parent, false);
    ConversationListItemClicked clickListener = (ConversationListItemClicked) context;
    return new ConversationListViewHolder(v, clickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull ConversationListViewHolder holder, int position) {
    ConversationWithNotificationItem item = chats.get(position);

  }

  @Override
  public int getItemCount() {
    return chats.size();
  }


  interface ConversationListItemClicked {
    void onConversationListItemClicked(int position, ConversationWithNotificationItem item);
  }

  class ConversationListViewHolder extends RecyclerView.ViewHolder {
    CircleImageView image;
    TextView personName, chatDesc, date;
    ConversationListItemClicked listener;
    int position;

    public ConversationListViewHolder(@NonNull View itemView, ConversationListItemClicked listener) {
      super(itemView);
      image = itemView.findViewById(R.id.other_persons_img);
      personName = itemView.findViewById(R.id.other_persons_name);
      chatDesc = itemView.findViewById(R.id.chat_description);
      date = itemView.findViewById(R.id.date);
      position = getAdapterPosition();
      ConversationWithNotificationItem item = chats.get(position);
      listener.onConversationListItemClicked(position, item);
    }
  }
}
