package com.pongo.zembe;

import android.app.Person;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pongo.zembe.MessagesFragment.TAG;


public class ConversationListRecyclerAdapter extends RecyclerView.Adapter<ConversationListRecyclerAdapter.ConversationListViewHolder> {
  GroundUser authenticatedUser;
  private Context context;
  private ArrayList<ConversationListItem> chats;

  public ConversationListRecyclerAdapter(Context context, ArrayList<ConversationListItem> chats) {
    this.context = context;
    this.chats = chats;
  }

  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
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
    ConversationListItem item = chats.get(position);
    PersonInChat conversationPartner = new PersonInChat();
    if (item.getAuthor().getUserPlatformID().equals(authenticatedUser.getUserDocumentID())) {
      conversationPartner = item.getOtherPerson();
    } else {
      conversationPartner = item.getAuthor();
    }
    String profileURL = conversationPartner.getProfilePictureURL();
    if(conversationPartner.getProfilePictureURL() != null){
      Picasso.get().load(profileURL).into(holder.image);
    }else{
      holder.image.setImageResource(R.drawable.gallamsey_photo_for_other);
    }

  }

  @Override
  public int getItemCount() {
    return chats.size();
  }

  interface ConversationListItemClicked {
    void onConversationListItemClicked(int position, ConversationListItem item);
  }

  class ConversationListViewHolder extends RecyclerView.ViewHolder {
    LinearLayout container;
    CircleImageView image;
    TextView personName, chatDesc, date;
    ConversationListItemClicked listener;


    public ConversationListViewHolder(@NonNull View itemView, final ConversationListItemClicked listener) {
      super(itemView);
      this.image = itemView.findViewById(R.id.other_persons_img);
      this.personName = itemView.findViewById(R.id.other_persons_name);
      this.chatDesc = itemView.findViewById(R.id.chat_description);
      this.date = itemView.findViewById(R.id.date);

      this.container = itemView.findViewById(R.id.container);
      container.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          int position = getAdapterPosition();
          ConversationListItem item = chats.get(position);
          listener.onConversationListItemClicked(position, item);
        }
      });

    }
  }
}

