package com.pongo.zembe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotificationFragment extends Fragment {

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//    View toolbarView = inflater.inflate(R.layout.app_default_toolbar,container,false);
    View notificationFragmentView =  inflater.inflate(R.layout.notification_nav_fragment,container,false);
//    TextView toolbarText = toolbarView.findViewById(R.id.toolbar_text);
//    toolbarText.setText("Notifications");
    return notificationFragmentView;
  }
}
