package com.pongo.zembe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingPage extends AppCompatActivity {

  LinearLayout layout;
  CircleImageView receipientImg;
  ImageView sendBtn, options;
  EditText textbox;
  RecyclerView recyclerView;
  GroundUser authenticatedUser, userOnTheOtherEnd;
  GenericErrandClass relatedErrand;
  String chatContext = Konstants.EMPTY;
  SimpleUser creator = new SimpleUser();
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  CollectionReference chatsDB = db.collection(Konstants.CHAT_COLLECTION);
  RequestQueue httpHandler;
  ConversationStream conversationStream; // Will always be updated with current chat content and all other information.
  LinearLayoutManager manager;
  String TAG = "LE-MESSAGING-PAGE";
  GalFirebaseHelper firebaseHelper = new GalFirebaseHelper();
  Boolean conversationIdExists = false;
  ProgressBar progressSpinner;
  TextView pageName;
  Context thisActivity;
  int initChatCount = 0;
  int currentChatCount = 0;
  int unreadCount;
  ChattingAdapter adapter;
  private View.OnClickListener profileIconClick = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      goToProfilePage();
    }
  };
  private View.OnClickListener sendMessage = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Log.d(TAG, conversationStream.toString());
      prepareAndSendMsg();
    }
  };
  private View.OnClickListener openDropDown = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      final PopupMenu menu = new PopupMenu(thisActivity, view);
      if (relatedErrand == null) {
        menu.inflate(R.menu.peer_to_peer_chat_menu);
      } else {
        menu.inflate(R.menu.errand_related_chat_menu);
      }

      menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
          switch (menuItem.getItemId()) {
            case R.id.about_errand: {
              seeMoreAboutErrandInQuestion();
              break;
            }
            case R.id.report: {
              Toast.makeText(thisActivity, "Report a person?", Toast.LENGTH_SHORT).show();
              break;
            }
            case R.id.back: {
              finish();
              break;
            }
            case R.id.more_about_receiver: {
              goToProfilePage();
              break;
            }
          }

          return true;
        }
      });
      menu.show();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    thisActivity = this;
    setContentView(R.layout.activity_chatting_page);
    httpHandler = Volley.newRequestQueue(this);
    pageName = findViewById(R.id.page_name);
    receipientImg = findViewById(R.id.profile_icon);
    getContentFromIntent();
    initializeActivity();
  }

  /**
   * ------------------- IMPORTANT SCENARIOS AND HOW THEY ARE APPROACHED ON THIS PAGE ---------------
   * There are a few cases that can lead a user into this page, and all of the cases are handled quick differently
   * onStart, but fit into the same process in the end
   * Scene 1 : A user randomly chooses a rider and and decides to message them directly to talk about anything
   * at all.
   * To determine the scene, "relatedErrand" has to be null, and "userOnOtherENd" has to have a valid user object
   * passed from the activity the user is from ...
   * <p>
   * Scene 2: A user tries to message a creator about an errand....
   * To determine this scene, "relatedErrand" has to be a valid Errand ....
   * <p>
   * Scene 3: A user comes to the chatting page by choosing a conversation list item from the conversation page
   * ...
   * THis case is different and easier, we always have to check for "EXISTING_CONVERSATION" and get the id of the
   * conversation stream, and just move on from there ...
   */

  public void getContentFromIntent() {
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    if (authenticatedUser == null) {
      authenticatedUser = new GroundUser();
      // will probably never happen, but still worth checking
//      goToLogin();
    }
    // ---- Is user coming from conversation fragment ? They will have a conversation id ready, don't go with the rest
    conversationIdExists = getIntent().getBooleanExtra(Konstants.EXISTING_CONVERSATION, false);
    final String streamID = getIntent().getStringExtra(Konstants.EXISTING_CONVERSATION_ID);
    if (conversationIdExists) {
      PersonInChat otherPerson = getIntent().getParcelableExtra(Konstants.USER_ON_THE_OTHER_END);
      relatedErrand = getIntent().getParcelableExtra(Konstants.PASS_ERRAND_AROUND);
      unreadCount = getIntent().getIntExtra(Konstants.UNREAD_COUNT, 0);
      putEndUserInformationOnPage(otherPerson);
      findAndFillStreamOnStart(streamID);
      return;
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

  public void putEndUserInformationOnPage(Object user) {
    if (user == null) return;
    if (user instanceof GroundUser) {
      Toast.makeText(this, "Yeaah, madafaka is a ground user", Toast.LENGTH_SHORT).show();
    } else if (user instanceof SimpleUser) {
      Toast.makeText(this, "Motherfucker is a simple user bro!", Toast.LENGTH_SHORT).show();
    } else if (user instanceof PersonInChat) {
      PersonInChat person = (PersonInChat) user;
      pageName.setText(person.getUserName());
      if (person.getProfilePictureURL() != null && !person.getProfilePictureURL().isEmpty()) {
        Picasso.get().load(person.getProfilePictureURL()).into(receipientImg);
      } else {
        Picasso.get().load(R.drawable.gallamsey_photo_for_other).into(receipientImg);
      }
    }
  }

  public void findAndFillStreamOnStart(final String streamID) {
    firebaseHelper.getDocumentWithField(Konstants.DB_QUERY_FIELD_CONVERSATION_ID, streamID, chatsDB, new GalInterfaceGuru.SnapshotTakerInterface() {
      @Override
      public void callback(DocumentSnapshot document) {
        toggleSpinner(false);
        conversationStream = document.toObject(ConversationStream.class);
        inflateRecyclerWithData(conversationStream);
        //---- set snapshot listener now to update the stream every time while user is on the page
        trackLiveMessagingUpdates(streamID);
      }
    });

  }

  public void trackLiveMessagingUpdates(String streamID) {
    firebaseHelper.setSnapshotListenerOnDocument(chatsDB.document(streamID), new GalInterfaceGuru.SnapshotTakerInterface() {
      @Override
      public void callback(DocumentSnapshot document) {
        ConversationStream updatedStream = document.toObject(ConversationStream.class);
        conversationStream = updatedStream;
        inflateRecyclerWithData(updatedStream);
      }
    });
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

    } else {
      findPeerConversationStream(streamDocumentListener);
    }
  }

  public void initializeActivity() {
    options = findViewById(R.id.options);
    options.setOnClickListener(openDropDown);
    progressSpinner = findViewById(R.id.progress_spinner);
    receipientImg.setVisibility(View.VISIBLE);
    receipientImg.setOnClickListener(profileIconClick);
    textbox = findViewById(R.id.textbox);
    sendBtn = findViewById(R.id.send_btn);
    sendBtn.setOnClickListener(sendMessage);
    inflateRecyclerWithData(new ConversationStream());
    //---------------------------------------------------------------------------------------------
    // if conversation ID exists, skip the rest of the checks, there is no need for the other steps
    if (conversationIdExists) return;
    //----------------------------------------------------------------------------------------------
    checkAndSeeIfConversationExists(new CollectConversationStream() {
      @Override
      public void getStream(ConversationStream conversation) {
        if (conversation != null) {
          Log.d(TAG, "Found an errand with your stuff");
          Toast.makeText(ChattingPage.this, "Found your errand", Toast.LENGTH_SHORT).show();
          conversationStream = conversation;
          toggleSpinner(false);
          toggleFirstTimerBox(false);
          inflateRecyclerWithData(conversation);
          // -------- update chats automatically if more come in ---------
          trackLiveMessagingUpdates(conversation.getConversationID());
        } else {
          toggleSpinner(false);
          toggleFirstTimerBox(true);
          //create new chat stream
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

  private void seeMoreAboutErrandInQuestion() {
    Intent page = new Intent(this, ErrandViewActivity.class);
    page.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
    page.putExtra(Konstants.PASS_ERRAND_AROUND, relatedErrand);
    startActivity(page);
  }

  public void goToProfilePage() {
    Intent page = new Intent(this, ViewProfilePage.class);
    page.putExtra(Konstants.AUTH_USER_KEY, authenticatedUser);
    startActivity(page);
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
    if (state) {
      welcomeBox.setVisibility(View.VISIBLE);
      return;
    }

    welcomeBox.setVisibility(View.GONE);
  }

  private void toggleSpinner(Boolean state) {
    if (state) {
      progressSpinner.setVisibility(View.VISIBLE);
    } else {
      progressSpinner.setVisibility(View.GONE);
    }
  }

  private void sendRequestToReduceUnread() {
    Log.d(TAG, "sendRequestToReduceUnread: Started sending reduction http....");
    if (unreadCount == 0) {
      Log.d(TAG, "sendRequestToReduceUnread: Unread is 0 so didnt send request");
      return;
    }
    JSONObject data = new JSONObject();
    try {
      data.put(Konstants.HTTP_DATA_VALUE_OWNER_ID, authenticatedUser.getUserDocumentID());
      data.put(Konstants.HTTP_DATA_VALUE_CONVERSATION_ID, conversationStream.getConversationID());
      data.put(Konstants.HTTP_DATA_VALUE_REDUCTION_NUM, unreadCount);
    } catch (Exception e) {
      e.printStackTrace();
    }

    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, GallamseyURLS.REDUCE_UNREAD_MSGS, data, null, null);
    httpHandler.add(req);

  }

  @Override
  protected void onDestroy() {
    sendRequestToReduceUnread();
    super.onDestroy();
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
        Log.d(TAG, conversationStream.toString());
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
        transaction.set(chatRef, stream);
        return null;
      }
    });
  }

  private int howManyDontBelongToMe(ArrayList<OneChatMessage> msgs) {
    if (msgs == null) return 0;
    int count = 0;
    for (int i = 0; i < msgs.size(); i++) {
      OneChatMessage one = msgs.get(i);
      if (!one.getUserPlatformID().equals(authenticatedUser.getUserDocumentID())) count++;
    }
    return count;
  }

  //--By default in Gallamsey DB, every msg a user receives is recorded as a +1 to a their unread msg
  //--Even when they are on the chat page and have read the text, the newly sent msgs will still count as +1 to unread
  //--This small fxn, is meant to count the new number of msgs that come through while the user is on the page and has read
  //--so that a request can be sent back to the server to reduce the unreadMsgsCount that has been recorded...
  //--the request is sent in "onDestroy"

  private void calculateUnReadMsgs(ArrayList<OneChatMessage> msgs) {
    if (msgs == null) return;
    currentChatCount = howManyDontBelongToMe(msgs);
    if (initChatCount == 0) initChatCount = msgs.size();
    else {
      unreadCount += currentChatCount - initChatCount;
    }

  }

  private void inflateRecyclerWithData(ConversationStream newConvo) {
    calculateUnReadMsgs(newConvo.getMessages());
    recyclerView = null;
    manager = null;
    recyclerView = findViewById(R.id.chatting_recycler);
    adapter= new ChattingAdapter(this, newConvo);
    adapter.setAuthenticatedUser(authenticatedUser);
    manager = new LinearLayoutManager(this);
    manager.setStackFromEnd(true);
    manager.setReverseLayout(false);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setVisibility(View.VISIBLE);
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

  interface CollectConversationStream {
    void getStream(ConversationStream conversation);
  }
}

