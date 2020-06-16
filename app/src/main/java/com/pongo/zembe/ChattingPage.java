package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ChattingPage extends AppCompatActivity {

  LinearLayout layout;
  RelativeLayout relativeLayout;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chatting_page);
    RecyclerView recyclerView = findViewById(R.id.chatting_recycler);
    ChattingAdapter adapter = new ChattingAdapter();
    LinearLayoutManager manager = new LinearLayoutManager(this);
//    manager.addView(relativeLayout);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
  }
}
