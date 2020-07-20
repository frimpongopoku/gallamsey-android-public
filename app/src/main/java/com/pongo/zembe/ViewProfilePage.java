package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewProfilePage extends AppCompatActivity {

  ImageView profilePicture;
  GroundUser authenticatedUser;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_profile_page);
    authenticatedUser = getIntent().getParcelableExtra("authUser");
    profilePicture = findViewById(R.id.profile_picture_full);
    if (authenticatedUser !=null){
      Picasso.get().load(authenticatedUser.getProfilePictureURL()).into(profilePicture);
    }

  }

  @Override
  public void finish() {
    super.finish();
    this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
  }
}


