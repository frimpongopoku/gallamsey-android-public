package com.pongo.zembe;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class GalFirebaseHelper {

  public void setSnapshotListenerOnDocument(DocumentReference reference, final GalInterfaceGuru.SnapshotTakerInterface takeShapshot) {
    reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
      @Override
      public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        takeShapshot.callback(documentSnapshot);
        if (e != null) {
          Log.d("errorCollectingSnapshot", e.getLocalizedMessage());
          e.printStackTrace();
        }
      }
    });
  }
}
