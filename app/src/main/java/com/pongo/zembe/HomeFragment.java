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

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements HomeNewsMultiAdapter.OnNewsItemClick {


  private FirebaseFirestore store = FirebaseFirestore.getInstance();
  private CollectionReference errandsDB = store.collection(Konstants.ERRAND_COLLECTION);
  private ArrayList<GenericErrandClass> news = new ArrayList<>();

  @Override
  public void setAllowEnterTransitionOverlap(boolean allow) {

  }


  public void setNews(ArrayList<GenericErrandClass> news) {
    this.news = news;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.home_nav_fragment, container, false);
    final ShimmerFrameLayout skeleton = view.findViewById(R.id.news_skeleton_view);
    skeleton.startShimmer();
    final HomeNewsMultiAdapter adapter = new HomeNewsMultiAdapter(getContext(), new ArrayList<GenericErrandClass>(), this);
    final RecyclerView recyclerView = view.findViewById(R.id.home_news_recycler);
    recyclerView.setAdapter(adapter);
    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(manager);
    getNewsFromFirebase(new NewsCollectionCallback() {
      @Override
      public void getErrands(ArrayList<GenericErrandClass> errands) {
        setNews(errands);
        adapter.setNews(errands);
        adapter.notifyDataSetChanged();
        skeleton.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
      }
    });
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
  public void callback(int pos) {
    GenericErrandClass errand = this.news.get(pos);
    Intent page = new Intent(getContext(),ErrandViewActivity.class);
    page.putExtra(Konstants.PASS_ERRAND_AROUND,errand);
    startActivity(page);
  }

  private interface NewsCollectionCallback {
    void getErrands(ArrayList<GenericErrandClass> errands);
  }
}
