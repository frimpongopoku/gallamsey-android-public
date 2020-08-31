package com.pongo.zembe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

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
  private RequestQueue myReq;
  public static final String TAG = "MESSAGING-FRAGMENT";
  private ProgressBar spinner ;
  private RelativeLayout relativeLayout;

  public MessagesFragment() {
  }

  public MessagesFragment(Context context) {
    this.context = context;
    this.stateTracker = (GalInterfaceGuru.TrackConversationListPage) context;
    this.myReq = Volley.newRequestQueue(context);
  }

  public void setOldState(View oldState, ArrayList<ConversationListItem> chatList){
    this.chatList = chatList;
    this.state = oldState;
  }

  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // means the old content of the last conversation list load was saved before user left the fragment
    // collect and show the same thing again, before looking for new conversations behind the scenes
    if (state != null) {
      inflateRecycler(chatList);
      return state;
    }
    View v = inflater.inflate(R.layout.messages_nav_fragment, container, false);
    this.state = v;
    View inflatedView = initialize(v);
    this.state = inflatedView;
    if(chatList != null && chatList.size() > 0){
      // this situation means, user has never visited the msg frag before so there is no view state available,
      // but homepage's periodic search has been able to collect conversations already, so there will be no need to search again
      // just create view, and inflate with content that homepage sent through....

      inflateRecycler(chatList);
      spinner.setVisibility(View.GONE);
    }else{
      getConversationsFromAbove();
    }
    return inflatedView;
  }

  private View initialize(View v) {
    recyclerView = v.findViewById(R.id.conversation_list_recycler);
    spinner = v.findViewById(R.id.spinner);
    relativeLayout = v.findViewById(R.id.no_chat_content);
    return v;
  }

  private void inflateRecycler(ArrayList<ConversationListItem> chats) {
    recyclerView = null;
    recyclerView = this.state.findViewById(R.id.conversation_list_recycler);
    LinearLayoutManager manager = new LinearLayoutManager(context);
    adapter = new ConversationListRecyclerAdapter(context, chats);
    adapter.setAuthenticatedUser(authenticatedUser);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setVisibility(View.VISIBLE);
  }

  private void getConversationsFromAbove(){
    JSONObject data = new JSONObject();
    try {
      data.put("user_id",authenticatedUser.getUserDocumentID());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonObjectRequest convoRequest = new JsonObjectRequest(Request.Method.POST, GallamseyURLS.FIND_ALL_CONVERSATIONS, data, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {

        ArrayList<ConversationListItem> conversations = processResponseData(response);
        chatList = conversations;
        spinner.setVisibility(View.GONE);
        Log.d(TAG, "onResponse: "+conversations.size());
        if(conversations.size() == 0){
          toggleNoChatContent(true);
        }else{
          toggleNoChatContent(false);
          inflateRecycler(conversations);
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse: "+error.getMessage());
      }
    });
    myReq.add(convoRequest);
  }

  private ArrayList<ConversationListItem> processResponseData(JSONObject response){
    ArrayList<ConversationListItem> conversations = new ArrayList<>();
    try {
      JSONObject error = (JSONObject) response.get("error");
      JSONArray content = (JSONArray) response.get("data");
      if(content != null){
        Gson gson = new Gson();
        for (int i = 0 ; i < content.length() ; i++){
          JSONObject conversation = (JSONObject) content.get(i);
          ConversationListItem convo =  gson.fromJson(conversation.toString(),ConversationListItem.class);
          Log.d(TAG, "onResponse: "+convo);
          conversations.add(convo);
        }
      }
    } catch (JSONException e) {
      Log.d(TAG, "onResponse: ERORR "+ e.getMessage());
    }

    return conversations;
  }

  private void toggleNoChatContent(Boolean state){
    if(state){
      relativeLayout.setVisibility(View.VISIBLE);
    }else{
      relativeLayout.setVisibility(View.GONE);
    }

  }


  @Override
  public void onDestroy() {
    stateTracker.saveConversationListState(this.chatList,this.state);
    super.onDestroy();
  }
}
