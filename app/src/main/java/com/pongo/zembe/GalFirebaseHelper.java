package com.pongo.zembe;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class GalFirebaseHelper {
public static final String TAG = "GALLAM-FIREBASE-HELPER";
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


  public void getDocumentWithField(String field, String value, CollectionReference collectionRef, final GalInterfaceGuru.SnapshotTakerInterface snapTaker){

    collectionRef.whereEqualTo(field,value).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot documents) {
        for (DocumentSnapshot document :documents) {
            if(document.exists()){
              snapTaker.callback(document);
              return;
            }
        }
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "onFailure: " + e.getMessage());
      }
    });

  }
}
