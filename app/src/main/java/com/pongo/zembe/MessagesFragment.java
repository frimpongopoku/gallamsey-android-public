package com.pongo.zembe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    if (state != null) {
      subscribeToListeners();
      return state;
    }

    View v = inflater.inflate(R.layout.messages_nav_fragment, container, false);
    this.state = v;
    View inflatedView = initialize(v);
    this.state = inflatedView;
    getConversationsFromAbove();
    return inflatedView;
  }

  private View initialize(View v) {
    recyclerView = v.findViewById(R.id.conversation_list_recycler);
    spinner = v.findViewById(R.id.spinner);
    return v;
  }

  private void inflateRecycler(ArrayList<ConversationListItem> chats) {
    recyclerView = null;
    recyclerView = this.state.findViewById(R.id.conversation_list_recycler);
    LinearLayoutManager manager = new LinearLayoutManager(context);
    adapter = new ConversationListRecyclerAdapter(context, chats);
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
        spinner.setVisibility(View.GONE);
        Log.d(TAG, "onResponse: "+conversations.size());
        inflateRecycler(conversations);
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
  private void subscribeToListeners() {

  }


}
