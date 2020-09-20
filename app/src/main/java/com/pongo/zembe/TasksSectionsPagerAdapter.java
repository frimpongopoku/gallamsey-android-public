package com.pongo.zembe;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TasksSectionsPagerAdapter extends FragmentPagerAdapter {

  String[] TAB_TITLES = {"Your Gigs", "Errands You Created"};
  private String[] TAB_KEYS = {Konstants.TASKS_GIGS_TAB, Konstants.TASKS_YOUR_ERRANDS_TAB};
  Context context;
  GroundUser authenticatedUser;

  public TasksSectionsPagerAdapter(Context context, @NonNull FragmentManager fm) {
    super(fm);
    this.context = context;
  }

  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return TasksFragmentGenerator.newInstance(TAB_KEYS[position],context,authenticatedUser);
  }


  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return TAB_TITLES[position];
  }

  @Override
  public int getCount() {
    return 2;
  }
}
