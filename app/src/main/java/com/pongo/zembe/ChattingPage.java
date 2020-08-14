package com.pongo.zembe;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ChattingPage extends AppCompatActivity {

  LinearLayout layout;
  ImageView receipientImg, sendBtn;
  EditText textbox;
  RecyclerView recyclerView;
  GroundUser authenticatedUser, userOnTheOtherEnd;
  Errand relatedErrand;
  String chatContext = Konstants.EMPTY;
  SimpleUser creator;
  PersonInChat author, otherPerson;
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  CollectionReference chatsDB = db.collection(Konstants.CHAT_COLLECTION);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chatting_page);
    getContentFromIntent();
    initializeActivity();
  }

  public void getContentFromIntent() {
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    if (authenticatedUser == null) {
      // will probably never happen, but still worth checking
      goToLogin();
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
      .whereArrayContains("involvedParties", authenticatedUser.getUniqueID())
      .get()
      .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot documents) {
          for (QueryDocumentSnapshot document : documents) {
            // "documents" is always going to be one item every time
            if (document.exists()) {
              streamDocumentListener.getStream(document);
            }
          }
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
    }
    findPeerConversationStream(streamDocumentListener);
  }

  public void initializeActivity() {
    receipientImg = findViewById(R.id.profile_icon);
    receipientImg.setVisibility(View.VISIBLE);
    textbox = findViewById(R.id.textbox);
    sendBtn = findViewById(R.id.send_btn);
    sendBtn.setOnClickListener(sendMessage);
    recyclerView = findViewById(R.id.chatting_recycler);
    ChattingAdapter adapter = new ChattingAdapter();
    LinearLayoutManager manager = new LinearLayoutManager(this);
    manager.setStackFromEnd(true);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);


  }


  private void goToLogin() {
    Intent page = new Intent(this, Login.class);
    startActivity(page);
    finish();
  }

  private View.OnClickListener sendMessage = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      String msg = textbox.getText().toString();
      textbox.setText("");
      Toast.makeText(ChattingPage.this, msg, Toast.LENGTH_SHORT).show();
    }
  };

  interface CollectConversationStream {
    void getStream(QueryDocumentSnapshot document);
  }
}

