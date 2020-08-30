package com.pongo.zembe;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TasksFragmentGenerator extends Fragment {
  public static final String TAG = "TASKS-FRAGMENT";
  private static String WHICH_FRAGMENT = "FRAGMENT_NAME";
  RelativeLayout narratorBox;
  RecyclerView recyclerView;
  ArrayList<GenericErrandClass> listOfErrands = new ArrayList<>(), listOfGigs = new ArrayList<>();
  View state;
  TextView salutationText;
  ProgressBar loadingSpinner;
  GroundUser authenticatedUser;
  Context context;
  RequestQueue httpHandler;
  int pageCheckPoint;
  String JOBS = "JOBS", CREATED = "CREATED";

  public static Fragment newInstance(String whichPage, Context context, GroundUser authenticatedUser) {
    TasksFragmentGenerator fragment = new TasksFragmentGenerator();
    fragment.setAuthenticatedUser(authenticatedUser);
    fragment.setContext(context);
    Bundle args = new Bundle();
    args.putString(WHICH_FRAGMENT, whichPage);
    fragment.setArguments(args);
    return fragment;
  }


  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    String whichPage = getArguments().getString(WHICH_FRAGMENT);
    httpHandler = Volley.newRequestQueue(context);
    if (whichPage != null && whichPage.equals(Konstants.TASKS_GIGS_TAB)) {
      View v = inflater.inflate(R.layout.gigs_layout, container, false);
      return initializeGigs(v);

    } else if (whichPage != null && whichPage.equals(Konstants.TASKS_YOUR_ERRANDS_TAB)) {
      View v = inflater.inflate(R.layout.your_created_errands_layout, container, false);
      return initializeYourErrands(v);
    }

    return null;
  }

  private View initializeGigs(View v) {
    loadingSpinner = v.findViewById(R.id.spinner);
    salutationText = v.findViewById(R.id.salutation);
    salutationText.setText(R.string.my_gigs_tab_no_content);
    narratorBox = v.findViewById(R.id.narrator_box);
    recyclerView = v.findViewById(R.id.gigs_layout_recycler);
    state = v;
    lookForMyErrands(JOBS);
    return v;
  }

  private View initializeYourErrands(View v) {
    loadingSpinner = v.findViewById(R.id.spinner);
    salutationText = v.findViewById(R.id.salutation);
    salutationText.setText(R.string.my_errands_tab_no_content);
    narratorBox = v.findViewById(R.id.narrator_box);
    recyclerView = v.findViewById(R.id.your_errands_recycler);
    state = v;
    lookForMyErrands(CREATED);
    return v;
  }

  private void inflateErrandsRecycler(ArrayList<GenericErrandClass> errands) {
    loadingSpinner.setVisibility(View.GONE);
    if (errands.size() == 0) {
      narratorBox.setVisibility(View.VISIBLE);
      return;
    }
    recyclerView = null;
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    recyclerView = state.findViewById(R.id.your_errands_recycler);
    YourErrandsTabRecyclerAdapter adapter = new YourErrandsTabRecyclerAdapter(getContext(), errands, (YourErrandsTabRecyclerAdapter.YourErrandItemClick) getContext());
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    recyclerView.setVisibility(View.VISIBLE);
  }

  private void inflateGigsRecycler(ArrayList<GenericErrandClass> gigs) {
    recyclerView = null;
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    recyclerView = state.findViewById(R.id.your_errands_recycler);
    GigsTabRecyclerAdapter adapter = new GigsTabRecyclerAdapter(getContext(), gigs, (GigsTabRecyclerAdapter.GigItemClick) getContext());
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
  }

  private void lookForMyErrands(final String context) {
    JSONObject data = new JSONObject();
    try {
      if (context.equals(CREATED)) {
        data.put("type", "created");
      } else {
        data.put("type", "jobs");
      }
      data.put(Konstants.HTTP_DATA_VALUE_USER_ID, authenticatedUser.getUserDocumentID());
      data.put(Konstants.HTTP_DATA_VALUE_CHECK_POINT, pageCheckPoint);

    } catch (Exception e) {
      e.printStackTrace();
    }

    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, GallamseyURLS.GET_MY_ERRANDS, data, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        ArrayList<GenericErrandClass> newContent = processResponseData(response);
        if (newContent == null) return;
        if (context.equals(CREATED)) {
          listOfErrands.addAll(newContent);
          inflateErrandsRecycler(listOfErrands);
          return;
        }
        listOfGigs.addAll(newContent);
        inflateErrandsRecycler(listOfGigs);

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse: " + error.toString());
      }
    });
    httpHandler.add(req);
  }

  private ArrayList<GenericErrandClass> processResponseData(JSONObject response) {
    ArrayList<GenericErrandClass> errandList = new ArrayList<>();
    try {
      JSONObject returnables = (JSONObject) response.get("returnables");
      pageCheckPoint = returnables != null ? (int) returnables.get("check_point") : 0;
      JSONObject error = (JSONObject) response.get("error");
      Boolean errStatus = (Boolean) error.get("status");
      if (errStatus) {
        Toast.makeText(context, "Sorry an error occurred: " + error.get("message"), Toast.LENGTH_SHORT).show();
        return null;
      }
      JSONArray data = (JSONArray) response.get("data");
      if (data != null) {
        Gson gson = new Gson();
        for (int i = 0; i < data.length(); i++) {
          JSONObject errandJson = (JSONObject) data.get(i);
          GenericErrandClass errand = gson.fromJson(errandJson.toString(), GenericErrandClass.class);
          errandList.add(errand);
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return errandList;
  }
}
