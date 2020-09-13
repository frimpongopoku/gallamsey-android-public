package com.pongo.zembe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteFragmentGenerator extends Fragment {


  private static String WHICH_FRAGMENT = "FRAGMENT_NAME";
  TemplateTrainForErrands allSavedTemplates;
  FavoriteRidersRecyclerAdapter adapter;
  TemplatesRecyclerAdapter templatesAdapter;
  Button loginBtn;
  Activity parentActivity;
  TextView salutationText;
  GroundUser authenticatedUser;
  RelativeLayout narratorBox;
  Context context;
  public static FavoriteFragmentGenerator newInstance(String whichFragment,GroundUser user) {
    FavoriteFragmentGenerator fragment = new FavoriteFragmentGenerator();
    Bundle args = new Bundle();
    args.putString(WHICH_FRAGMENT, whichFragment);
    fragment.setArguments(args);
    fragment.setAuthenticatedUser(user);
    return fragment;
  }

  private View.OnClickListener goToLoginPage = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent page = new Intent(parentActivity,Login.class);
      startActivity(page);
      parentActivity.finish();

    }
  };


  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }


  private View authOrNot(View v, String which){
    loginBtn = v.findViewById(R.id.login_btn);
    salutationText = v.findViewById(R.id.salutation);
    narratorBox = v.findViewById(R.id.narrator_box);
    if(authenticatedUser == null){
      String t = "Hi there, please Sign In to see your favorite riders and templates.";
      salutationText.setText(t);
      narratorBox.setVisibility(View.VISIBLE);
      loginBtn.setOnClickListener(goToLoginPage);
      loginBtn.setVisibility(View.VISIBLE);
      return v;
    }
    if(which.equals(Konstants.USER_TEMPLATES_TAB)) return  initializeTemplatesTab(v);
    return initializeFavoritesTab(v);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    parentActivity = this.getActivity();
    String whichFragment = null;
    whichFragment = getArguments().getString(WHICH_FRAGMENT);
    if (whichFragment.equals(Konstants.USER_TEMPLATES_TAB)) {
      View v = LayoutInflater.from(getContext()).inflate(R.layout.errand_templates_fragment, container, false);

      return authOrNot(v, Konstants.USER_TEMPLATES_TAB);
    } else if (whichFragment.equals(Konstants.FAVORITE_RIDERS_TAB)) {

      View v = LayoutInflater.from(getContext()).inflate(R.layout.favorite_riders_fragment, container, false);

      return authOrNot(v,Konstants.FAVORITE_RIDERS_TAB);
    }
    return null;
  }

  private View initializeTemplatesTab(View v) {
    RelativeLayout narratorBox = v.findViewById(R.id.narrator_box);
    TextView text = v.findViewById(R.id.salutation);
    if (getContext() != null) {
      allSavedTemplates = (TemplateTrainForErrands) MyHelper.getFromSharedPreferences(getContext(), Konstants.SAVE_ERRANDS_AS_TEMPLATE, TemplateTrainForErrands.class);
    }
    if (allSavedTemplates == null) {
      allSavedTemplates = new TemplateTrainForErrands();
    }
    RecyclerView recycler = v.findViewById(R.id.templates_recycler);
    if(allSavedTemplates.getErrands().size() == 0){
      String msg = "Hello there, it doesn't look like you have any templates yet. All your templates will appear here...";
      text.setText(msg);
      recycler.setVisibility(View.GONE);
      narratorBox.setVisibility(View.VISIBLE);
    }
    templatesAdapter = new TemplatesRecyclerAdapter(getContext(), allSavedTemplates.getErrands(), (TemplatesRecyclerAdapter.TemplateItemClick) getContext());
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    recycler.setLayoutManager(manager);
    new ItemTouchHelper(templateSwipeFunctionality).attachToRecyclerView(recycler);
    recycler.setAdapter(templatesAdapter);
    return v;
  }

  private View initializeFavoritesTab(View v) {
    RecyclerView recycler = v.findViewById(R.id.fav_riders_recycler);
    adapter = new FavoriteRidersRecyclerAdapter(getContext(), new ArrayList<SimpleUser>(), (FavoriteRidersRecyclerAdapter.RidersItemClick) getContext());
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    recycler.setLayoutManager(manager);
    new ItemTouchHelper(favRiderSwipeFunctionality).attachToRecyclerView(recycler);
    recycler.setAdapter(adapter);
    return v;
  }

  private ItemTouchHelper.SimpleCallback templateSwipeFunctionality = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
      return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
      int pos = viewHolder.getAdapterPosition();
      GenericErrandClass errand = allSavedTemplates.getErrands().get(pos);
      templatesAdapter.notifyItemRemoved(pos);
      allSavedTemplates.removeFromArray(errand);
      if (getContext() != null) {
        //treating data as immutable
        MyHelper.removeFromSharedPreference(getContext(), Konstants.SAVE_ERRANDS_AS_TEMPLATE); //removed old stuff
        if (allSavedTemplates.getErrands().size() > 0) {
          //dont save anything if this is the last item
          MyHelper.saveToSharedPreferences(getContext(), allSavedTemplates, Konstants.SAVE_ERRANDS_AS_TEMPLATE);
        }
        Toast.makeText(getContext(), errand.getTitle() + " is removed from your list", Toast.LENGTH_SHORT).show();
      }
    }
  };
  private ItemTouchHelper.SimpleCallback favRiderSwipeFunctionality = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
      return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
      Toast.makeText(getContext(), "Rider Removed - " + viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();

    }
  };


}
