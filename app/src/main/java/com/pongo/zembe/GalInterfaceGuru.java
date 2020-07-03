package com.pongo.zembe;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class GalInterfaceGuru {
  interface SnapshotTakerInterface {
    void callback(DocumentSnapshot document);
  }

  interface PaymentContactAdapterClickable {
    void callback(int position);
  }

}
