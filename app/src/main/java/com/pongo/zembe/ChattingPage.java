package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChattingPage extends AppCompatActivity {

  LinearLayout layout;
  RelativeLayout relativeLayout;
  ImageView receipientImg, sendBtn;
  EditText textbox;
  RecyclerView recyclerView;
  GroundUser authenticatedUser;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chatting_page);
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    initializeActivity();
  }

  public void initializeActivity() {
    receipientImg = findViewById(R.id.profile_icon);
    receipientImg.setVisibility(View.VISIBLE);
    textbox = findViewById(R.id.textbox);
    sendBtn = findViewById(R.id.send_btn);
    sendBtn.setOnClickListener(sendMessage);
    recyclerView = findViewById(R.id.chatting_recycler);
    ChattingAdapter adapter = new ChattingAdapter();
    LinearLayoutManager manager = new LinearLayoutManager(this);
    manager.setStackFromEnd(true);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);


  }

  private View.OnClickListener textboxActivation = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      try {
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
      }
      catch (Exception e){
        Log.d("scrollRecyclerToBottom:", e.toString());
      }
    }
  };
  private  View.OnClickListener sendMessage = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      String msg = textbox.getText().toString();
      textbox.setText("");
      Toast.makeText(ChattingPage.this, msg, Toast.LENGTH_SHORT).show();
    }
  };


}
