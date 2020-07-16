package com.pongo.zembe;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

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

  public void setSnapshotListenerOnFolder(CollectionReference reference, final GalInterfaceGuru.FolderTakerInterface folderTakerInterface){
    reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
      @Override
      public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
          folderTakerInterface.callback(queryDocumentSnapshots);
          if(e !=null){
            Log.d("errorCollectingFolder",e.getLocalizedMessage());
            e.printStackTrace();;
          }
      }
    });
  }
}
