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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {


  public static final String TAG = "HOME-FRAGMENT";
  ArrayList<GenericErrandClass> oldNews;
  View currentState;
  GalInterfaceGuru.TrackHomeFragmentState fragmentStateListener;
  HomeNewsMultiAdapter adapter;
  RecyclerView recyclerView;
  ShimmerFrameLayout skeleton;
  GalFirebaseHelper firebaseHelper = new GalFirebaseHelper();
  GroundUser authenticatedUser;
  Context context;
  JSONObject returnables;
  MagicBoxes dialogCreator;
  private FirebaseFirestore store = FirebaseFirestore.getInstance();
  private CollectionReference errandsDB = store.collection(Konstants.ERRAND_COLLECTION);
  private ArrayList<GenericErrandClass> news = new ArrayList<>();
  private RequestQueue httpHandler;

  public HomeFragment() {
  }

  //----- because of the backend structure, there is no way, news will come up empty, so there is no need to check for that anywhere
  public HomeFragment(ArrayList<GenericErrandClass> news, View oldViewState, GalInterfaceGuru.TrackHomeFragmentState fragmentStateListener) {
    this.news = news;
    this.currentState = oldViewState;
    this.fragmentStateListener = fragmentStateListener;
    this.context = (Context) fragmentStateListener;
    this.httpHandler = Volley.newRequestQueue(this.context);
    this.dialogCreator = new MagicBoxes(this.context);
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
    if ((this.news == null || this.news.size() == 0) && authenticatedUser != null) {
      skeleton.setVisibility(View.VISIBLE);

    }
    setCurrentState(view);
    //  For the first time : getNewsHere();
    JSONObject data = fashionRequestData();
    getNewsContent(data, new NewsCollectionCallback() {
      @Override
      public void getErrands(ArrayList<GenericErrandClass> errands) {
        news = errands;
        skeleton.setVisibility(View.GONE);
        inflateRecycler(errands);
        Log.d(TAG, "getErrands: " + errands.toString());

      }
    });
    return view;
  }


  private void inflateRecycler(ArrayList<GenericErrandClass> content) {
    recyclerView = null;
    recyclerView = this.currentState.findViewById(R.id.home_news_recycler);
    this.adapter = new HomeNewsMultiAdapter(getContext(), content, (HomeNewsMultiAdapter.OnNewsItemClick) getContext());
    this.adapter.setAuthenticatedUser(authenticatedUser);
    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setVisibility(View.VISIBLE);

  }

  //---------------------------------------------------------------------------------------------------------------
  // If a user is authenticated, this function will ust put their country, region, and coordinates together
  // so that they can get news feed according to their location
  // However, if they are not signed in, a popup box will show and ask them to choose country & Region quickly...
  //---------------------------------------------------------------------------------------------------------------
  private JSONObject fashionRequestData() {
    JSONObject data = new JSONObject();
    try {
      if (authenticatedUser != null) {
        data.put("country", authenticatedUser.getCountry());
        //data.put("region",authenticatedUser.getRegion());
        //--- coords will follow here too
        return data;
      } else {
        // --- create a dialog that will collect country and region on the fly...
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void getNewsContent(JSONObject requestData, final NewsCollectionCallback newsCollector) {
    if (requestData == null) {
      Toast.makeText(context, "Sorry, could not get news for you, please try again later", Toast.LENGTH_SHORT).show();
      return;
    }
    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, GallamseyURLS.GET_NEWS_CONTENT, requestData, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        ArrayList<GenericErrandClass> errands = processResponseData(response);
        newsCollector.getErrands(errands);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse: " + error.getMessage());
      }
    });
    httpHandler.add(req);
  }


  private ArrayList<GenericErrandClass> processResponseData(JSONObject response) {
    try {
      ArrayList<GenericErrandClass> news = new ArrayList<>();
      JSONObject error = (JSONObject) response.get("error");
      Boolean status = (Boolean) error.get("status");
      this.returnables = (JSONObject) response.get("returnables");
      if (status) return null;
      JSONArray data = (JSONArray) response.get("data");
      Gson gson = new Gson();
      for (int i = 0; i < data.length(); i++) {
        String errandAsText = data.get(i).toString();
        GenericErrandClass errand = gson.fromJson(errandAsText, GenericErrandClass.class);
        news.add(errand);
      }
      return news;
    } catch (JSONException e) {
      e.printStackTrace();
    }


    return null;

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

  @Override
  public void onResume() {
    super.onResume();
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
