package com.pongo.zembe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

  HashMap<String, ArrayList<String>> items;
  ArrayList<String> test = new ArrayList<>();
  FirebaseFirestore store = FirebaseFirestore.getInstance();
  CollectionReference errandsDB = store.collection(Konstants.ERRAND_COLLECTION);

  @Override
  public void setAllowEnterTransitionOverlap(boolean allow) {

  }

  public HomeFragment(HashMap<String, ArrayList<String>> items) {
    this.items = items;

  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.home_nav_fragment, container, false);
    final ShimmerFrameLayout skeleton = view.findViewById(R.id.news_skeleton_view);
    skeleton.startShimmer();
    HomeNewsMultiAdapter adapter = new HomeNewsMultiAdapter(getContext(), new ArrayList<GenericErrandClass>());
    final RecyclerView recyclerView = view.findViewById(R.id.home_news_recycler);
    recyclerView.setAdapter(adapter);
    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(manager);
    getNewsFromFirebase(new NewsCollectionCallback() {
      @Override
      public void getErrands(ArrayList<GenericErrandClass> errands) {
        HomeNewsMultiAdapter newsAdapter = new HomeNewsMultiAdapter(getContext(), errands);
        recyclerView.setAdapter(newsAdapter);
        skeleton.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
      }
    });
//    new Handler().postDelayed(new Runnable() {
//      @Override
//      public void run() {
//        skeleton.stopShimmer();
//        skeleton.setVisibility(View.GONE);
//        recyclerView.setVisibility(View.VISIBLE);
//      }
//    }, 2000);
    return view;
  }

  private void getNewsFromFirebase(final NewsCollectionCallback newsCallback) {
    errandsDB.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        ArrayList<GenericErrandClass> list = new ArrayList<>();
        for (DocumentSnapshot document : queryDocumentSnapshots) {
          list.add(document.toObject(GenericErrandClass.class));
        }

        newsCallback.getErrands(list);
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        e.printStackTrace();
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private interface NewsCollectionCallback {
    void getErrands(ArrayList<GenericErrandClass> errands);
  }
}
