package com.pongo.zembe;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.pongo.zembe.MessagesFragment.TAG;

public class ChattingAdapter extends RecyclerView.Adapter {

  private Context context;
  private ConversationStream conversation = new ConversationStream();
  private GroundUser authenticatedUser;

  public ChattingAdapter(Context context, ConversationStream conversation) {
    this.context = context;
    this.conversation = conversation;
  }

  public GroundUser getAuthenticatedUser() {
    return authenticatedUser;
  }

  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }

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
      RecipientViewHolder holder = new RecipientViewHolder(view);
      return holder;
    }
  }

  @Override
  public int getItemViewType(int position) {
    OneChatMessage msg = conversation.getMessages().get(position);
    if (msg.getUserPlatformID().equals(authenticatedUser.getUserDocumentID())) {
      return Konstants.SENDER_VIEW_TYPE;

    } else {
      return Konstants.RECEIPIENT_VIEW_TYPE;
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    OneChatMessage msg = conversation.getMessages().get(position);
    if (msg.getUserPlatformID().equals(authenticatedUser.getUserDocumentID())) {
      setSenderContent(holder, position);
    } else {
      setRecipientContent(holder, position);
    }

  }

  public void setSenderContent(@NonNull RecyclerView.ViewHolder _holder, int position) {
    OneChatMessage msg = conversation.getMessages().get(position);
    SenderViewHolder holder = (SenderViewHolder) _holder;
//    holder.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in_while_moving_right));
    holder.msg.setText(msg.getMessage());
    holder.date.setText(DateHelper.getOnlyHoursAndMinutesFromDate(msg.getTimeStamp()));

  }

  public void setRecipientContent(@NonNull RecyclerView.ViewHolder _holder, int position) {
    OneChatMessage msg = conversation.getMessages().get(position);
    RecipientViewHolder holder = (RecipientViewHolder) _holder;
//    holder.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in_while_moving_right));
    holder.msg.setText(msg.getMessage());
    holder.date.setText(DateHelper.getOnlyHoursAndMinutesFromDate(msg.getTimeStamp()));

  }

  @Override
  public int getItemCount() {
    return conversation.getMessages().size();
  }


  public class SenderViewHolder extends RecyclerView.ViewHolder {
    TextView date, msg;
    LinearLayout container;

    public SenderViewHolder(@NonNull View itemView) {
      super(itemView);
      date = itemView.findViewById(R.id.date);
      msg = itemView.findViewById(R.id.msg_content);
      container = itemView.findViewById(R.id.container);
    }
  }

  public class RecipientViewHolder extends RecyclerView.ViewHolder {
    TextView date, msg;
    LinearLayout container;
    public RecipientViewHolder(@NonNull View itemView) {
      super(itemView);
      date = itemView.findViewById(R.id.date);
      msg = itemView.findViewById(R.id.msg_content);
      container = itemView.findViewById(R.id.container);
    }
  }
}
