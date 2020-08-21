package com.pongo.zembe;

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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class MessagesFragment extends Fragment {

  FirebaseFirestore db = FirebaseFirestore.getInstance();
  CollectionReference chatListCollectionRef = db.collection(Konstants.CHAT_LIST_COLLECTION);

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v =  inflater.inflate(R.layout.messages_nav_fragment,container,false);
    LinearLayout chatListItem = (LinearLayout) v.findViewById(R.id.chat_item);
    chatListItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent messageSomeone = new Intent( getContext(),ChattingPage.class);
        startActivity(messageSomeone);
      }
    });
    return v;
  }


  private void resetReadValue(String documentId){
    DocumentReference docRef = chatListCollectionRef.document(documentId); 
    docRef.update("unReadMsgs",0);
  }



}
