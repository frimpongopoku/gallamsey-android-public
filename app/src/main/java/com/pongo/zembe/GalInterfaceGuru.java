package com.pongo.zembe;

import android.net.Uri;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class GalInterfaceGuru {
  interface SnapshotTakerInterface {
    void callback(DocumentSnapshot document);
  }

  interface PaymentContactAdapterClickable {
    void callback(int position);
  }

  interface TagDialogChipActions {
    void removeTag(View v);
  }
  interface CollectUploadPictureURL{
    void collectURI (Uri uri);
  }

  interface  CollectErrandTrainFormShipment{
    void getErrandObject(GenericErrandClass errand);
  }
  interface TrackHomeFragmentState{
    void saveFragmentState(ArrayList<GenericErrandClass> news,View view);
  }interface TrackWalletFragmentState{
    void saveFragmentState(View view);
  }
}
