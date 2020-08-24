package com.pongo.zembe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {

  RecyclerView recyclerView;
  ConversationListRecyclerAdapter adapter;
  GalFirebaseHelper firebaseHelper = new GalFirebaseHelper();
  ArrayList<MsgNotificationTracker> notificationDocuments = new ArrayList<>();
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private CollectionReference chatListCollectionRef = db.collection(Konstants.CHAT_LIST_COLLECTION);
  private CollectionReference msgNotificationRef = db.collection(Konstants.MSG_NOTIFICATION_COLLECTION);
  private GroundUser authenticatedUser;
  private View state;
  private Context context;
  private GalInterfaceGuru.TrackConversationListPage stateTracker;
  private ArrayList<ConversationListItem> chatList = new ArrayList<>();

  public MessagesFragment() {
  }

  public MessagesFragment(Context context, View oldState) {
    this.context = context;
    this.stateTracker = (GalInterfaceGuru.TrackConversationListPage) context;
    this.state = oldState;
  }

  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    if (state != null) {
      subscribeToListeners();
      return state;
    }
    View v = inflater.inflate(R.layout.messages_nav_fragment, container, false);
    state = v;
    View inflatedView = initialize(v);
    state = inflatedView;
    return inflatedView;
  }

  private View initialize(View v) {
    subscribeToListeners();
    return v;
  }

  private void inflateRecycler(ArrayList<ConversationListItem> chats) {
    recyclerView = null;
    recyclerView = state.findViewById(R.id.conversation_list_recycler);
    LinearLayoutManager manager = new LinearLayoutManager(context);
    adapter = new ConversationListRecyclerAdapter(context, chats);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
  }

  private void subscribeToListeners() {

  }


}
