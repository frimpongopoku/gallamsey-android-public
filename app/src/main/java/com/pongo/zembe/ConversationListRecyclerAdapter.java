package com.pongo.zembe;

import android.app.Person;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    Errand errand = item.getRelatedErrand();
    String date = item.getTimestamp();
    if (item.getAuthor().getUserPlatformID().equals(authenticatedUser.getUserDocumentID())) {
      conversationPartner = item.getOtherPerson();
    } else {
      conversationPartner = item.getAuthor();
    }
    String profileURL = conversationPartner.getProfilePictureURL();
    if (conversationPartner.getProfilePictureURL() != null && !profileURL.isEmpty()) {
      Picasso.get().load(profileURL).into(holder.image);
    } else {
      holder.image.setImageResource(R.drawable.gallamsey_photo_for_other);
    }
    int unread = item.getUnReadMsgs();
    if (unread != 0) {
      holder.unReadBadge.setVisibility(View.VISIBLE);
      holder.unReadBadge.setText(String.valueOf(unread));
    }
    String otherPersonName = conversationPartner.getUserName();
    if (otherPersonName != null) {
      holder.personName.setText(otherPersonName);
    } else {
      holder.personName.setText("loading...");
    }

    if(item.getLastMessage() != null){
      String msg = item.getLastMessage().getMessage();
      msg = msg != null ? msg : "We are trying to load the content that was just sent, but we are having some difficulty...";
      holder.chatDesc.setText(msg);
    }
    if(date != null){
      holder.date.setText(DateHelper.getTimeAgo(date));
    }
    if(errand !=null){
      holder.pricingBox.setVisibility(View.VISIBLE);
      holder.cost.setText(String.valueOf(errand.getCost()));
      holder.allowance.setText(String.valueOf(errand.getAllowance()));
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
    TextView personName, chatDesc, date, unReadBadge, cost, allowance;
    ConversationListItemClicked listener;
    RelativeLayout pricingBox;


    public ConversationListViewHolder(@NonNull View itemView, final ConversationListItemClicked listener) {
      super(itemView);
      this.image = itemView.findViewById(R.id.other_persons_img);
      this.personName = itemView.findViewById(R.id.other_persons_name);
      this.chatDesc = itemView.findViewById(R.id.chat_description);
      this.date = itemView.findViewById(R.id.date);
      this.unReadBadge = itemView.findViewById(R.id.unread_count);
      this.pricingBox = itemView.findViewById(R.id.pricing_box);
      this.container = itemView.findViewById(R.id.container);
      this.cost = itemView.findViewById(R.id.errand_cost);
      this.allowance = itemView.findViewById(R.id.errand_allowance);
      container.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          unReadBadge.setVisibility(View.GONE);
          int position = getAdapterPosition();
          ConversationListItem item = chats.get(position);
          listener.onConversationListItemClicked(position, item);
        }
      });

    }
  }
}

