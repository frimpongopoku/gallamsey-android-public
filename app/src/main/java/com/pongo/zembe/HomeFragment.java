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
  Context context = getContext();


  public HomeFragment(ArrayList<GenericErrandClass> news,View oldViewState, GalInterfaceGuru.TrackHomeFragmentState fragmentStateListener) {
    this.news = news;
    this.currentState = oldViewState;
    this.fragmentStateListener = fragmentStateListener;

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
    if(currentState !=null){
      return currentState;
    }
    View view = inflater.inflate(R.layout.home_nav_fragment, container, false);
    recyclerView = view.findViewById(R.id.home_news_recycler);
    skeleton = view.findViewById(R.id.news_skeleton_view);
    adapter = new HomeNewsMultiAdapter(getContext(), news, (HomeNewsMultiAdapter.OnNewsItemClick) getContext());
    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    if (this.news == null || this.news.size() == 0) {
      skeleton.setVisibility(View.VISIBLE);
      skeleton.startShimmer();
    }
//    For the first time : getNewsHere();
    getNewsFromFirebase(new NewsCollectionCallback() {
      @Override
      public void getErrands(ArrayList<GenericErrandClass> errands) {
        setNews(errands);
        adapter.setNews(errands);
        adapter.notifyDataSetChanged();
        skeleton.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        setNews(errands);
      }
    });
    setCurrentState(view);
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

  @Override
  public void onResume() {
    super.onResume();


  }

  @Override
  public void onDestroy() {
    this.fragmentStateListener.saveFragmentState(news,currentState);
    super.onDestroy();
  }

//  @Override
//  public void newsItemCallback(int pos) {
//    GenericErrandClass errand = this.news.get(pos);
//    Intent page = new Intent(context, ErrandViewActivity.class);
//    page.putExtra(Konstants.PASS_ERRAND_AROUND, errand);
//    startActivity(page);
//  }

  private interface NewsCollectionCallback {
    void getErrands(ArrayList<GenericErrandClass> errands);
  }
}
