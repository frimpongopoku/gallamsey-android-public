package com.pongo.zembe.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pongo.zembe.FavoriteFragmentGenerator;
import com.pongo.zembe.Konstants;
import com.pongo.zembe.R;
import com.pongo.zembe.TempatesRecyclerAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {


  private String[] TAB_TITLES = {"Errand Templates","Your Favorite Riders"};
  private String[] TAB_KEYS = {Konstants.USER_TEMPLATES_TAB,Konstants.FAVORITE_RIDERS_TAB};
  private final Context mContext;

  public SectionsPagerAdapter(Context context, FragmentManager fm) {
    super(fm);
    mContext = context;
  }

  @Override
  public Fragment getItem(int position) {
    // getItem is called to instantiate the fragment for the given page.
    // Return a PlaceholderFragment (defined as a static inner class below).
    return FavoriteFragmentGenerator.newInstance(TAB_KEYS[position]);
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return  TAB_TITLES[position];
  }

  @Override
  public int getCount() {
    // Show 2 total pages.
    return 2;
  }
}