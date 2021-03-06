package com.pongo.zembe;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
  private CollectionReference communitiesDB = store.collection(Konstants.COMMUNITIES_COLLECTION);
  private ArrayList<GenericErrandClass> news = new ArrayList<>();
  private RequestQueue httpHandler;
  private NewsCacheHolder newsCacher;
  private SwipeRefreshLayout refresher;
  private LinearLayoutManager manager;
  private ProgressBar spinner;
  private Boolean isLoading = false;
  private float alpha = 1;
  private GalFirebaseHelper firebaseHelper = new GalFirebaseHelper();
  private Type newsCacheType = new TypeToken<NewsCacheHolder>() {
  }.getType();
  private Spinner countryDropdown, regionsDropdown;
  private LinearLayout regionsContainer;
  private TextView regionsText;
  private Communities communities;
  private LoadedCommunities communityTracker = new LoadedCommunities();
  private Activity parentActivity = this.getActivity();
  private AnonymousUser anonymousUser = new AnonymousUser();
  private String selectedCountry, selectedRegion;
  private Gson gson = new Gson();
  private AdapterView.OnItemSelectedListener selectRegion = new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      selectedRegion = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
  };
  private AdapterView.OnItemSelectedListener selectCountry = new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      final String country = adapterView.getItemAtPosition(i).toString();
      selectedCountry = country;
      if (country.equals("GHANA")) {
        regionsContainer.setVisibility(View.VISIBLE);
        // if the communities of the selected country have already been loaded, dont go to firestore again
        Communities alreadyLoadedAndSaved = communityTracker.getSavedCommunitiesForCountry(country);
        if (alreadyLoadedAndSaved != null) {
          MyHelper.initializeDropDown(alreadyLoadedAndSaved.getCommunityNames(), regionsDropdown, context);
          return;
        }
        String t = "We are loading regions...";// doing it this way just to skip android studio lint yellow lines
        regionsText.setText(t);
        firebaseHelper.getFirebaseDocument(country, communitiesDB, new GalInterfaceGuru.SnapshotTakerInterface() {
          @Override
          public void callback(DocumentSnapshot document) {
            communities = document.toObject(Communities.class);
            if (communities != null) {
              communityTracker.addCommunity(country, communities);
              String t = "What region are you in?";
              regionsText.setText(t);
              MyHelper.initializeDropDown(communities.getCommunityNames(), regionsDropdown, context);
            }
          }
        });

      } else {
        regionsContainer.setVisibility(View.GONE);
      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
  };
  private ResponseHandler lastResponse = ResponseHandler.newInstance();
  private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(@NonNull final RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);
      if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
        if (!isLoading) {
          spinner.setVisibility(View.VISIBLE);
          isLoading = true;
          getNewsContent(fashionRequestData(), new NewsCollectionCallback() {
            @Override
            public void getErrands(ArrayList<GenericErrandClass> errands) {
              int newsSizeBefore = news.size();
              news.addAll(errands);
              adapter.notifyItemRangeChanged(newsSizeBefore - 1, news.size());
              spinner.setVisibility(View.GONE);
              isLoading = false;
            }
          });
        }
      }
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
    parentActivity = this.getActivity();
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

    if (this.news == null || this.news.size() == 0) {
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
    JSONObject data = fashionRequestData();
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
    skeleton.setVisibility(View.GONE);

  }

  private LinearLayoutManager getSpeedyLinearManager() {
    return new LinearLayoutManager(context) {
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
  private JSONObject fashionRequestData() {
    int checkPoint = 0, fallbackCheckPoint = 0;

    final JSONObject data = new JSONObject();
    try {
//      if (lastResponse.getReturnables() != null) {
////        checkPoint = returnables.getInt("check_point");
////        fallbackCheckPoint = returnables.getInt("fallback_check_point");
//      } else {
//        data.put("check_point", checkPoint);
//        data.put("fallback_check_point", fallbackCheckPoint);
//      }

      if (authenticatedUser != null) {
        data.put("country", authenticatedUser.getCountry());
        //data.put("region",authenticatedUser.getRegion());
        //--- coords will follow here too
        return data;
      } else {
        // --- create a dialog that will collect country and region on the fly if user hasnt done that already...
        anonymousUser = (AnonymousUser) MyHelper.getFromSharedPreferences(context, Konstants.ANONYMOUS_USER, AnonymousUser.class);
        if (anonymousUser == null) {
          collectQuickDataForGuestUser();
        }
        data.put("country", anonymousUser.getCountry());
        data.put("region", anonymousUser.getRegion());
        return data;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void collectQuickDataForGuestUser() {
    View v = View.inflate(context, R.layout.country_and_gender_dialog, null);
    regionsContainer = v.findViewById(R.id.regions_container);
    countryDropdown = v.findViewById(R.id.country_dropdown);
    regionsDropdown = v.findViewById(R.id.regions_dropdown);
    regionsDropdown.setOnItemSelectedListener(selectRegion);
    regionsText = v.findViewById(R.id.regions_text);
    countryDropdown.setOnItemSelectedListener(selectCountry);
    MyHelper.initializeDropDown(Konstants.COUNTRIES, countryDropdown, context);
    MagicBoxes dialog = new MagicBoxes(context);
    dialog.constructCustomDialog("Quick Setup", v, new MagicBoxCallables() {
      @Override
      public void negativeBtnCallable() {
        parentActivity.finish();
      }

      @Override
      public void positiveBtnCallable() {
        skeleton.setVisibility(View.VISIBLE);
        anonymousUser = new AnonymousUser();
        anonymousUser.setCountry(selectedCountry);
        anonymousUser.setRegion(selectedRegion);
        MyHelper.saveToSharedPreferences(context, anonymousUser, Konstants.ANONYMOUS_USER);
      }
    }, "Done", "Quit").show();

  }


  private void getNewsContent(JSONObject requestData, final NewsCollectionCallback newsCollector) {
    if (requestData == null) {
      Toast.makeText(context, "Sorry, could not get news for you, please try again later", Toast.LENGTH_SHORT).show();
      isLoading = false;
      spinner.setVisibility(View.GONE);
      return;
    }
    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, GallamseyURLS.GET_NEWS_CONTENT, requestData, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        ResponseHandler nResponse =  ResponseHandler.newInstance(response);
        lastResponse = nResponse;
        Log.d(TAG, "onResponse: "+nResponse.toString());
        ArrayList<GenericErrandClass> errands = processResponseData(nResponse);
        if (errands != null) {
          skeleton.setVisibility(View.GONE);
          newsCacher.getNews().addAll(errands);
          MyHelper.saveToSharedPreferences(context, newsCacher, Konstants.SAVE_NEWS_TO_CACHE);
        }
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

  private ArrayList<GenericErrandClass> processResponseData(ResponseHandler response) {
    ArrayList<GenericErrandClass> news = new ArrayList<>();
    try {
      if(response.getError().getStatus()) return null;
      Gson gson = new Gson();
      for (int i = 0; i < response.getData().getContent().length(); i++) {
        String errandAsText = response.getData().getContent().get(i).toString();
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
