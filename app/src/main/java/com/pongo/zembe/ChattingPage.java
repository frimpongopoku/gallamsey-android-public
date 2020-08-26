package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChattingPage extends AppCompatActivity {

  LinearLayout layout;
  ImageView receipientImg, sendBtn;
  EditText textbox;
  RecyclerView recyclerView;
  GroundUser authenticatedUser, userOnTheOtherEnd;
  Errand relatedErrand;
  String chatContext = Konstants.EMPTY;
  SimpleUser creator = new SimpleUser();
  PersonInChat author, otherPerson;
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  CollectionReference chatsDB = db.collection(Konstants.CHAT_COLLECTION);
  RequestQueue volley;
  ConversationStream conversationStream;
  LinearLayoutManager manager;
  String TAG = "LE-MESSAGE";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chatting_page);
    volley = Volley.newRequestQueue(this);
    getContentFromIntent();
    initializeActivity();
  }


  public void getContentFromIntent() {
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    if (authenticatedUser == null) {
      authenticatedUser = new GroundUser();
      // will probably never happen, but still worth checking
//      goToLogin();
    }
    relatedErrand = getIntent().getParcelableExtra(Konstants.PASS_ERRAND_AROUND);
    if (relatedErrand != null) {
      // then it means this chat is about an errand, so just set the chat context to "ABOUT_AN_ERRAND"
      chatContext = Konstants.ABOUT_AN_ERRAND;
      creator = relatedErrand.getCreator();
    } else {
      // means its just a normal chat between two people on the platform just doing shit we dont know ...
      chatContext = Konstants.PEER_TO_PEER;
      userOnTheOtherEnd = getIntent().getParcelableExtra(Konstants.USER_ON_THE_OTHER_END);

    }
  }

  public void findErrandRelatedStream(final CollectConversationStream streamDocumentListener) {
    chatsDB
      .whereEqualTo("relatedErrand.errandDocumentID", relatedErrand.getErrandDocumentID())
      .whereArrayContains("involvedParties", authenticatedUser.getUserDocumentID())
      .get()
      .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot documents) {
          for (QueryDocumentSnapshot document : documents) {
            // "documents" is always going to be one item every time
            if (document.exists()) {
              ConversationStream convo = document.toObject(ConversationStream.class);
              streamDocumentListener.getStream(convo);
              return;
            }
          }
          streamDocumentListener.getStream(null);
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.d("findingChatStreamErr:", e.getMessage());
        }
      });
  }

  public void findPeerConversationStream(final CollectConversationStream streamDocumentListener) {
    if (authenticatedUser == null || userOnTheOtherEnd == null) {
      streamDocumentListener.getStream(null);

    }

    //search mongodb, and find the id of the conversation stream,
    //then bring the chat document (all because firestore does not allow the query we need here)


  }

  public void checkAndSeeIfConversationExists(CollectConversationStream streamDocumentListener) {
    if (chatContext.equals(Konstants.ABOUT_AN_ERRAND)) {
      findErrandRelatedStream(streamDocumentListener);

    }else{
      findPeerConversationStream(streamDocumentListener);
    }
  }

  public void initializeActivity() {
    receipientImg = findViewById(R.id.profile_icon);
    receipientImg.setVisibility(View.VISIBLE);
    textbox = findViewById(R.id.textbox);
    sendBtn = findViewById(R.id.send_btn);
    sendBtn.setOnClickListener(sendMessage);
    inflateRecyclerWithData(new ConversationStream());
    checkAndSeeIfConversationExists(new CollectConversationStream() {
      @Override
      public void getStream(ConversationStream conversation) {
        if (conversation != null) {
          Log.d(TAG,"Found an errand with your stuff");
          Toast.makeText(ChattingPage.this, "Found your errand", Toast.LENGTH_SHORT).show();
          conversationStream = conversation;
          Log.d(TAG,conversationStream.toString());
          toggleFirstTimerBox(false);
          inflateRecyclerWithData(conversation);
        } else {
          //create new chat stream
          Log.d(TAG,"Just checking how youa re doing....");
          conversationStream = new ConversationStream();
          conversationStream.setAuthor(createChatPersonFromGround(authenticatedUser));
          conversationStream.setOtherPerson(createChatPersonFromGround(creator));
          conversationStream.setRelatedErrand(relatedErrand);
          conversationStream.setConversationContext(chatContext);
          ArrayList<String> involvedParties = new ArrayList<>();
          involvedParties.add(authenticatedUser.getUserDocumentID());
          involvedParties.add(creator.getUserPlatformID());
          conversationStream.setInvolvedParties(involvedParties);
        }
      }
      // setup recycler based on just retrieved messages

    });
  }

  private OneChatMessage createMsgFromText(String text) {
    OneChatMessage msg = new OneChatMessage();
    msg.setMessage(text);
    msg.setUserPlatformID(authenticatedUser.getUserDocumentID());
    msg.setTimeStamp(DateHelper.getDateInMyTimezone());
    return msg;
  }


  private void prepareAndSendMsg() {
    // validate text box content, but don't show any errors
    String msg = MyHelper.grabCleanText(textbox);
    if (msg.isEmpty()) {
      return;
    }
    OneChatMessage chatMsg = createMsgFromText(msg);
    // add message to conversation stream locally

    conversationStream.addMessage(chatMsg);
    Log.d(TAG, conversationStream.toString());
    // update the Firestore document that represents this stream, so the other user can see
    updateMessagingStreamInFirestore(chatMsg);
    // add updated conversation stream to recycler items
    inflateRecyclerWithData(conversationStream);


  }

  private void toggleFirstTimerBox(Boolean state) {
    RelativeLayout welcomeBox = findViewById(R.id.first_timer_box);
    if(state){
      welcomeBox.setVisibility(View.VISIBLE);
      return;
    }

    welcomeBox.setVisibility(View.GONE);
  }

  private void cleanup() {
    textbox.setText("");
  }

  private void updateMessagingStreamInFirestore(final OneChatMessage message) {
    toggleFirstTimerBox(false);
    cleanup();
    db.runTransaction(new Transaction.Function<Void>() {
      @Nullable
      @Override
      public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
        String id = conversationStream.getConversationID();
        Log.d(TAG,conversationStream.toString());
        if (id == null) {
          //means this is the first time so create stream instead
          DocumentReference streamRef = chatsDB.document();
          String conversationID = streamRef.getId();
          conversationStream.setConversationID(conversationID);
          streamRef.set(conversationStream);
          // create snapshot listener for just created stream

          return null;
        }
        // create ref to firestore reference to current chat document
        DocumentReference chatRef = chatsDB.document(id);
        // read latest version of chat stream
        DocumentSnapshot chatSnap = transaction.get(chatRef);
        // convert snapshot to our conversation stream obj and get all messages that are available at the time
        ConversationStream stream = chatSnap.toObject(ConversationStream.class);
        ArrayList<OneChatMessage> allMsgs = stream.getMessages();
        allMsgs.add(message);
        stream.setMessages(allMsgs);
        // updated timestamp
        stream.setTimestamp(DateHelper.getDateInMyTimezone());
        //update with the new message the user wants to send
//       transaction.update(chatRef,"messages",allMsgs);
       transaction.set(chatRef,stream);
        return null;
      }
    });
  }

  private void inflateRecyclerWithData(ConversationStream newConvo) {
    recyclerView = null;
    manager = null;
    recyclerView = findViewById(R.id.chatting_recycler);
    ChattingAdapter adapter = new ChattingAdapter(this, newConvo);
    adapter.setAuthenticatedUser(authenticatedUser);
    manager = new LinearLayoutManager(this);
    manager.setStackFromEnd(true);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
  }

  private PersonInChat createChatPersonFromGround(GroundUser user) {
    PersonInChat person = new PersonInChat();
    person.setProfilePictureURL(user.getProfilePictureURL());
    person.setUserName(user.getPreferredName());
    person.setUserPlatformID(user.getUserDocumentID());
    person.setLastSeen(DateHelper.getDateInMyTimezone());
    return person;
  }

  private PersonInChat createChatPersonFromGround(SimpleUser user) {
    PersonInChat person = new PersonInChat();
    person.setProfilePictureURL(user.getProfilePicture());
    person.setUserName(user.getUserName());
    person.setUserPlatformID(user.getUserPlatformID());
    person.setLastSeen(DateHelper.getDateInMyTimezone());
    return person;
  }


  private void goToLogin() {
    Intent page = new Intent(this, Login.class);
    startActivity(page);
    finish();
  }

  private View.OnClickListener sendMessage = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Log.d(TAG, "Before it begun....");
      Log.d(TAG, conversationStream.toString());
      prepareAndSendMsg();
    }
  };


  public void urlTestDrive() {
    JsonObjectRequest peerRequest = new JsonObjectRequest(Request.Method.POST, GallamseyURLS.TEST_URL, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        Log.d("FROMSERVER:", response.toString());
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d("FROMSERVERERROR:", error.getMessage());
      }
    });
    volley.add(peerRequest);

  }

  interface CollectConversationStream {
    void getStream(ConversationStream conversation);
  }
}

