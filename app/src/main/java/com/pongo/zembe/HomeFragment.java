package com.pongo.zembe;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {


  private FirebaseFirestore store = FirebaseFirestore.getInstance();
  private CollectionReference errandsDB = store.collection(Konstants.ERRAND_COLLECTION);
  private ArrayList<GenericErrandClass> news = new ArrayList<>();
  ArrayList<GenericErrandClass> oldNews;
  View currentState;
  GalInterfaceGuru.TrackHomeFragmentState fragmentStateListener;
  HomeNewsMultiAdapter adapter;
  RecyclerView recyclerView;
  ShimmerFrameLayout skeleton;
  GalFirebaseHelper firebaseHelper = new GalFirebaseHelper();
  GroundUser authenticatedUser;


  public HomeFragment(){}
  public HomeFragment(ArrayList<GenericErrandClass> news, View oldViewState, GalInterfaceGuru.TrackHomeFragmentState fragmentStateListener) {
    this.news = news;
    this.currentState = oldViewState;
    this.fragmentStateListener = fragmentStateListener;

  }

  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }

  public void setCurrentState(View currentState) {
    this.currentState = currentState;
  }

  public void setNews(ArrayList<GenericErrandClass> news) {
    this.news = news;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.home_nav_fragment, container, false);
    justInflate(view);
    // Return old view state and items when the user comes back to the fragment again.
    // we don't want multiple loading for reasons as just switching between fragments
    //so return old view and recycler items, that were saved in root activity "onDestroy",
    //but still check for new items in "onResume" and fill the recycler up if there are any new stuff
    // NOTE: inflation comes before view is returned for  a reason(view items are null if justInflate comes later on)
    if (currentState != null) {
      return currentState;
    }
    //------ Loading for the first time, show beautiful shimmer effect and load data
    if (this.news == null || this.news.size() == 0) {
      skeleton.setVisibility(View.VISIBLE);

    }
    //  For the first time : getNewsHere();
    getNewsFromFirebase(new NewsCollectionCallback() {
      @Override
      public void getErrands(ArrayList<GenericErrandClass> errands) {
        setNews(errands);
        adapter.setNews(errands);
        adapter.notifyDataSetChanged();
        skeleton.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
      }
    });
    setCurrentState(view);
    return view;
  }


  private void justInflate(View view) {
    recyclerView = view.findViewById(R.id.home_news_recycler);
    skeleton = view.findViewById(R.id.news_skeleton_view);
    this.adapter = new HomeNewsMultiAdapter(getContext(), news, (HomeNewsMultiAdapter.OnNewsItemClick) getContext());
    this.adapter.setAuthenticatedUser(authenticatedUser);
    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
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

  @Override
  public void onResume() {
    super.onResume();
    firebaseHelper.setSnapshotListenerOnFolder(errandsDB, new GalInterfaceGuru.FolderTakerInterface() {
      @Override
      public void callback(QuerySnapshot documents) {
        ArrayList<GenericErrandClass> newErrands = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
          if (document.exists()) {
            GenericErrandClass tempErrand = document.toObject(GenericErrandClass.class);
            newErrands.add(tempErrand);
          }

        }
        setNews(newErrands);
        adapter.setNews(newErrands);
        adapter.notifyDataSetChanged();
      }
    });

  }

  @Override
  public void onDestroy() {
    this.fragmentStateListener.saveFragmentState(news, currentState);
    super.onDestroy();
  }


  private interface NewsCollectionCallback {
    void getErrands(ArrayList<GenericErrandClass> errands);
  }
}
