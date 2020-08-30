package com.pongo.zembe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class ViewProfilePage extends AppCompatActivity {

  public static final String TAG = "PROFILE-PAGE";
  TextView verifiedText,userFullName, emailAndPhone ,goldNumber, silverNumber, bronzeNumber, trashNumber, jobsTaken, jobsCreated, fans, pageName;
  ImageView profilePicture, optionsBtn,verifiedIcon;
  GroundUser authenticatedUser, foundUser;
  String userPlatformID;
  RequestQueue httpHandler;
  ProgressBar spinner;
  LinearLayout contentBox, verifiedBox;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_profile_page);
    httpHandler = Volley.newRequestQueue(this);
    userPlatformID = getIntent().getStringExtra(Konstants.USER_PLATFORM_ID);
//    userPlatformID = "HSfG4yw0fuX0rRsbJ1TBmmbjEKN2";
    authenticatedUser = getIntent().getParcelableExtra(Konstants.AUTH_USER_KEY);
    profilePicture = findViewById(R.id.profile_picture_full);
    initializeActivity();
    if (userPlatformID != null) {
      getUserInformation();
    }
    if (authenticatedUser != null) {
      spinner.setVisibility(View.GONE);
      inflatePageWithUser(authenticatedUser);
    }


  }

  private GroundUser processResponseData(JSONObject response) {
    try {
      Gson gson = new Gson();
      JSONObject error = (JSONObject) response.get("error");
      Boolean status = (Boolean) error.get("status");
      if (status) {
        String msg = (String) error.get("message");
        Log.d(TAG, "processResponseData: " + msg);
        Toast.makeText(this, "An Error occurred, sorry. Try coming back to this page later", Toast.LENGTH_SHORT).show();
        return null;
      }
      JSONObject userJson = (JSONObject) response.get("data");
      GroundUser user = gson.fromJson(userJson.toString(), GroundUser.class);
      return user;
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void getUserInformation() {
    JSONObject data = new JSONObject();
    try {
      data.put(Konstants.HTTP_DATA_VALUE_USER_ID, userPlatformID);
      JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, GallamseyURLS.FIND_USER_PROFILE, data, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          Log.d(TAG, "onResponse: " + response.toString());
          foundUser = processResponseData(response);
        }
      }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          Toast.makeText(ViewProfilePage.this, "Ooops!-" + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });

      httpHandler.add(req);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void setVerified(String status){
    if(status.equals(Konstants.VERFIED)){
      verifiedIcon.setColorFilter(ContextCompat.getColor(this,R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
      verifiedText.setText("Verified");
      return;
    }
    verifiedIcon.setColorFilter(ContextCompat.getColor(this,R.color.shimmer_color), android.graphics.PorterDuff.Mode.SRC_IN);
  }
  private void takeCareOfProfilePicture(String url){
    if(url == null){
      profilePicture.getLayoutParams().height = 70;
      profilePicture.setAlpha((float) 0);
    }
    Picasso.get().load(url).into(profilePicture);
  }
  private void inflatePageWithUser(GroundUser user){
    takeCareOfProfilePicture(user.getProfilePictureURL());
    GlorifyMe myRewards = new GlorifyMe(user.getAccolades());
    goldNumber.setText(myRewards.getGoldMedals()+"");
    silverNumber.setText(myRewards.getSilverMedals()+"");
    bronzeNumber.setText(myRewards.getBronzeMedals()+"");
    jobsCreated.setText(user.getAccolades().getErrandCount() +" Jobs Created");
    jobsTaken.setText(user.getAccolades().getGigsCount() +" Jobs Taken");
    fans.setText(0 +" Fans");
    trashNumber.setText(myRewards.getJunkCups()+"");
    contentBox.setVisibility(View.VISIBLE);
    String email = user.getEmail();
    Toast.makeText(this, user.getEmail(), Toast.LENGTH_LONG).show();
    String phone = user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty() ? " | "+user.getPhoneNumber() : "";
    emailAndPhone.setText(email + phone);
    userFullName.setText(user.getPreferredName());
    setVerified(user.getVerifiedStatus());
  }

  private void initializeActivity() {
    verifiedText = findViewById(R.id.verified_text);
    verifiedBox = findViewById(R.id.verified_box);
    verifiedIcon = findViewById(R.id.verified_icon);
    contentBox = findViewById(R.id.content_box);
    spinner = findViewById(R.id.progress_spinner);
    userFullName = findViewById(R.id.user_full_name);
    emailAndPhone = findViewById(R.id.email_and_phone);
    optionsBtn = findViewById(R.id.options);
    optionsBtn.setVisibility(View.GONE);
    pageName = findViewById(R.id.page_name);
    goldNumber = findViewById(R.id.gold_number);
    silverNumber = findViewById(R.id.silver_number);
    bronzeNumber = findViewById(R.id.bronze_number);
    trashNumber = findViewById(R.id.trash_number);
    jobsTaken = findViewById(R.id.jobs_taken);
    jobsCreated = findViewById(R.id.jobs_created);
    fans = findViewById(R.id.fans);
  }

  @Override
  public void finish() {
    super.finish();
    this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
  }
}


