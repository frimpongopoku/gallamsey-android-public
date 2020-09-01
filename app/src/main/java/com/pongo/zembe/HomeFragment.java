package com.pongo.zembe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

interface NewsCollectionCallback {
  void getErrands(ArrayList<GenericErrandClass> errands);
}

public class HomeFragment extends Fragment {


  public static final String TAG = "HOME-FRAGMENT";
  private ArrayList<GenericErrandClass> oldNews;
  private View currentState;
  private GalInterfaceGuru.TrackHomeFragmentState fragmentStateListener;
  private HomeNewsMultiAdapter adapter;
  private RecyclerView recyclerView;
  private ShimmerFrameLayout skeleton;
  private GroundUser authenticatedUser;
  private Context context;
  private JSONObject returnables;
  private MagicBoxes dialogCreator;
  private FirebaseFirestore store = FirebaseFirestore.getInstance();
  private CollectionReference errandsDB = store.collection(Konstants.ERRAND_COLLECTION);
  private ArrayList<GenericErrandClass> news = new ArrayList<>();
  private RequestQueue httpHandler;
  private NewsCacheHolder newsCacher;
  private SwipeRefreshLayout refresher;
  private LinearLayoutManager manager;
  private ProgressBar spinner;
  private Boolean isLoading = false;
  private float alpha = 1;
  private Type newsCacheType = new TypeToken<NewsCacheHolder>() {
  }.getType();
  private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(@NonNull final RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);
      if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
        if (!isLoading) {
          spinner.setVisibility(View.VISIBLE);
          isLoading = true;
          getNewsContent(fashionRequestData(0), new NewsCollectionCallback() {
            @Override
            public void getErrands(ArrayList<GenericErrandClass> errands) {
              int newsSizeBefore = news.size();
              news.addAll(errands);
              adapter.notifyItemRangeChanged(newsSizeBefore -1, news.size());
              spinner.setVisibility(View.GONE);
              isLoading = false;
            }
          });
        }
      }
    }
  };
  private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
      JSONObject data = fashionRequestData(0);
      getNewsContent(data, new NewsCollectionCallback() {
        @Override
        public void getErrands(ArrayList<GenericErrandClass> errands) {
          setNews(errands);
          skeleton.setVisibility(View.GONE);
          inflateRecycler(errands, currentState);
          refresher.setRefreshing(false);
        }
      });
    }
  };

  public HomeFragment() {
  }

  //----- GENERAL NB : Because of the backend structure, there is no way, news will come up empty, so there is no need to check for that anywhere
  public HomeFragment(ArrayList<GenericErrandClass> news, View oldViewState, GalInterfaceGuru.TrackHomeFragmentState fragmentStateListener) {
    this.news = news; // could be new content generated from splash screen, or content saved from fragment on destroy
    this.currentState = oldViewState; // just the view state saved from fragment on destroy
    this.fragmentStateListener = fragmentStateListener;
    this.context = (Context) fragmentStateListener;
    this.httpHandler = Volley.newRequestQueue(this.context);
    this.dialogCreator = new MagicBoxes(this.context);
    this.newsCacher = (NewsCacheHolder) MyHelper.getFromSharedPreferences(this.context, Konstants.SAVE_NEWS_TO_CACHE, newsCacheType);
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

    setCurrentState(view);
    //------ Loading for the first time, show beautiful shimmer effect and load data
    if ((this.news == null || this.news.size() == 0) && authenticatedUser != null) {
      // checking for user authentication to show shimmer effect because if they are not signed in,
      // a different thing will be shown instead of shimmer on first load
      // ----- Now, check and see if there is any cached content
      if (newsCacher != null && newsCacher.getNews().size() > 0) {
        setNews(newsCacher.getNews());
        inflateRecycler(newsCacher.getNews(), view); // set the users view with cached content quickly, and still move on to look for new stuff
      } else {
        skeleton.setVisibility(View.VISIBLE);
      }

    }

    //------ Normal content fetching when a user first launches, or clicks on the home tab ---------
    JSONObject data = fashionRequestData(0);
    getNewsContent(data, new NewsCollectionCallback() {
      @Override
      public void getErrands(ArrayList<GenericErrandClass> errands) {
        setNews(errands);
        skeleton.setVisibility(View.GONE);
        inflateRecycler(errands, currentState);
      }
    });
    return view;
  }

  private void inflateRecycler(ArrayList<GenericErrandClass> content, View view) {
    recyclerView = null;
    recyclerView = view.findViewById(R.id.home_news_recycler);
    this.adapter = new HomeNewsMultiAdapter(getContext(), content, (HomeNewsMultiAdapter.OnNewsItemClick) getContext());
    this.adapter.setAuthenticatedUser(authenticatedUser);
    manager = getSpeedyLinearManager();
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    recyclerView.addOnScrollListener(scrollListener);
    recyclerView.setHasFixedSize(true);
    recyclerView.setVisibility(View.VISIBLE);

  }

  private LinearLayoutManager getSpeedyLinearManager() {
   return  new LinearLayoutManager(context) {
      @Override
      public void scrollToPosition(int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(context) {

          private static final float SPEED = 400f;// Change this value (default=25f)

          @Override
          protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return SPEED / displayMetrics.densityDpi;
          }

        };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);

      }
    };
  }

  //---------------------------------------------------------------------------------------------------------------
  // If a user is authenticated, this function will  put their country, region, and coordinates together
  // so that they can get news feed according to their location
  // However, if they are not signed in, a popup box will show and ask them to choose country & Region quickly...
  //---------------------------------------------------------------------------------------------------------------
  private JSONObject fashionRequestData(int checkPoint) {
    JSONObject data = new JSONObject();
    try {
      data.put("check_point", checkPoint);
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
        NewsCacheHolder newsCache = new NewsCacheHolder(errands);
        MyHelper.saveToSharedPreferences(context, newsCache, Konstants.SAVE_NEWS_TO_CACHE);
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
//    refresher = view.findViewById(R.id.refresher);
//    refresher.setOnRefreshListener(refreshListener);
    skeleton = view.findViewById(R.id.news_skeleton_view);
    recyclerView = view.findViewById(R.id.home_news_recycler);
    spinner = view.findViewById(R.id.spinner);
    inflateRecycler(new ArrayList<GenericErrandClass>(), view);
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
}
