package com.pongo.zembe;

import android.net.Uri;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GalInterfaceGuru {
  interface SnapshotTakerInterface {
    void callback(DocumentSnapshot document);
  }

  interface  TemplatingLimitExceededError{
    void callback(String error);
  }
  interface PaymentContactAdapterClickable {
    void callback(int position);
  }

  interface TagDialogChipActions {
    void removeTag(View v);
  }

  interface CollectUploadPictureURL {
    void collectURI(Uri uri);
  }

  interface CollectErrandTrainFormShipment {
    void getErrandObject(GenericErrandClass errand);
  }

  interface TrackHomeFragmentState {
    void saveFragmentState(ArrayList<GenericErrandClass> news, View view);
  }
  interface  FolderTakerInterface{
    void callback(QuerySnapshot documents);
  }

  interface TrackWalletFragmentState {
    void saveWalletState(ArrayList<Object>transactions,View view);
  }

  interface  EditContextMenuItemListener{
    void getErrandToBeEdited(int pos, GenericErrandClass errand);
  }

  interface  MessageCreatorContextMenuItemListener{
    void talkToCreatorAboutErrand(int pos, GenericErrandClass errand);
  }

  interface TrackConversationListPage{
    void saveConversationListState(ArrayList<ConversationListItem> chats, View state);
  }
}
