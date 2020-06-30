package com.pongo.zembe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CreateNewErrand extends AppCompatActivity implements OnDetailItemsClick {
  Toolbar toolbar;
  Button teachMe, addDetails;
  Switch imageSwitcher;
  ImageView errandImageHolder;
  LinearLayoutManager manager;
  RecyclerView recyclerView;
  DetailsListAdapter detailsListAdapter;
  ArrayList<String> detailsArray;
  EditText detailsBox, errandDescriptionbox, errandTitleBox, errandAllowancebox, errandCostBox;
  Spinner expiryDateDropDown;
  ImageUploadHelper imageHelper;
  Bitmap resizedUploadableImageBitmap = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_new_errand);
    toolbar = findViewById(R.id.app_default_toolbar);
    setSupportActionBar(toolbar);
    initialize();

  }

  public void initialize() {
    expiryDateDropDown = findViewById(R.id.expiry_date_dropdown);
    ArrayAdapter<CharSequence> expiryAdapter = ArrayAdapter.createFromResource(this, R.array.errand_expiry_values, android.R.layout.simple_spinner_item);
    expiryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    expiryDateDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String value = adapterView.getItemAtPosition(i).toString();
        double hours = DateHelper.getHoursValueFromDurationString(value);
        Log.w("getJumpedDate:",DateHelper.getDateInMyTimezone());
        Log.w("getJumpedDate:",DateHelper.jumpDateByHours(DateHelper.getDateInMyTimezone(),hours));
        expiryDateDropDown.requestFocus();
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
    expiryDateDropDown.setAdapter(expiryAdapter);
    errandDescriptionbox = findViewById(R.id.errand_description);
    errandTitleBox = findViewById(R.id.errand_title);
    errandAllowancebox = findViewById(R.id.errand_allowance);
    errandCostBox = findViewById(R.id.errand_estimate_cost);
    errandImageHolder = findViewById(R.id.errand_image_holder);
    errandImageHolder.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        imageHelper = new ImageUploadHelper(getApplicationContext());
        imageHelper.openFileChooser(new ImageUploadHelper.FileChooserCallback() {
          @Override
          public void getBackChooserIntent(Intent intent) {
            startActivityForResult(intent, Konstants.CHOOSE_IMAGE_REQUEST_CODE);
          }
        });
      }
    });
    imageSwitcher = findViewById(R.id.image_switcher);
    imageSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
          errandImageHolder.setVisibility(View.VISIBLE);
          errandImageHolder.requestFocus();
        } else {
          resizedUploadableImageBitmap = null;
          errandImageHolder.setVisibility(View.GONE);
        }
      }
    });
//    -----------------------------------------------------

    teachMe = findViewById(R.id.errand_creation_info);
    teachMe.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent teachMePage = new Intent(CreateNewErrand.this, TeachMeHowToCreateAnErrand.class);
        startActivity(teachMePage);
      }
    });
//    -----------------------------------------------------
    detailsArray = new ArrayList<>();
    detailsListAdapter = new DetailsListAdapter(this, detailsArray, this);
    recyclerView = findViewById(R.id.details_recycler_list);
    manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(detailsListAdapter);
//  -----------------------------------------------------
    detailsBox = findViewById(R.id.details_textbox);
    addDetails = findViewById(R.id.add_details_btn);
    addDetails.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String val = detailsBox.getText().toString();
        if (!val.isEmpty()) {
          detailsArray.add(val);
          detailsListAdapter.notifyDataSetChanged();
          detailsBox.setText("");
        }
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Konstants.CHOOSE_IMAGE_REQUEST_CODE && data.getData() != null && resultCode == RESULT_OK) {
      Uri uri = data.getData();
      imageHelper.compressImage(uri, new ImageUploadHelper.CompressedImageCallback() {
        @Override
        public void getCompressedImage(Bitmap compressedBitmap) {
          errandImageHolder.getLayoutParams().height = 400;
          errandImageHolder.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
          errandImageHolder.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
          errandImageHolder.requestLayout();
          errandImageHolder.setImageBitmap(compressedBitmap);
          errandImageHolder.requestFocus();
          resizedUploadableImageBitmap = compressedBitmap;
        }
      });

    }
  }

  public Boolean validateContent() {
    Boolean allGood = true;
    String desc, cost, allowance, title;
    desc = errandDescriptionbox.getText().toString().trim();
    cost = errandCostBox.getText().toString().trim();
    allowance = errandAllowancebox.getText().toString().trim();
    title = errandTitleBox.getText().toString().trim();
    //------ validate errand description
    if (desc.isEmpty()) {
      allGood = false;
      errandDescriptionbox.setError("You cannot create an errand without a description");
      errandDescriptionbox.requestFocus();
    }
    //------ validate cost
    if (cost.isEmpty()) {
      allGood = false;
      errandCostBox.setError("How much will everything cost? Put the exact number");
      errandCostBox.requestFocus();
    }
    //-------- validate allowance
    if (allowance.isEmpty()) {
      allGood = false;
      errandAllowancebox.setError("How much would you like to give whoever runs your errand");
      errandAllowancebox.requestFocus();
    }
    //--------- validate title
    if (title.isEmpty()) {
      allGood = false;
      errandTitleBox.setError("Provide a title for your post");
      errandTitleBox.requestFocus();
    }


    return allGood;
  }

  @Override
  public void onDetailItemClick(int pos) {
    detailsArray.remove(pos);
    detailsListAdapter.notifyDataSetChanged();
    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    detailsBox.requestFocus();
  }
}
