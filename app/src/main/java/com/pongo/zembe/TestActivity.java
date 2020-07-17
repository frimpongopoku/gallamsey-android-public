package com.pongo.zembe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestActivity extends AppCompatActivity {

  CircleImageView image;
  Button btn;
  ImageUploadHelper imageUploadHelper;
  Activity activity;
  EditText words;
  ChipGroup group;
  AutoCompleteTextView autoTextbox;
  MagicBoxes boxCreator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    btn = findViewById(R.id.pic_btn);
    image = findViewById(R.id.image);
    activity = this;
    imageUploadHelper = new ImageUploadHelper(this);
    words = findViewById(R.id.words);
    boxCreator = new MagicBoxes(this);

    autoTextbox = findViewById(R.id.auto_complete);
    final String[] arr = {"Ghana", "Land", "Of Freedom", "zidane", "Keyna", "Gambia"};
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
    autoTextbox.setAdapter(adapter);


    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
//        Location loc = new Location(Konstants.INIT_STRING);
//        loc.setLongitude(Konstants.sLong);
//        loc.setLatitude(Konstants.sLat);

        GallamseyLocationComponent loc = new GallamseyLocationComponent("Test",Konstants.sLong, Konstants.sLat);


        Log.d("locTesting:", "Distance: " + loc.getRadius());
        Log.d("locTesting:", loc.getLocation().toString());
        Log.d("locTesting:", loc.getEndPoint().toString());
      }
    });

  }

  public Chip createChip(final String name) {
    Chip chip = new Chip(this);
    chip.setText(name);
    chip.setCloseIconEnabled(true);
    chip.setOnCloseIconClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(activity, "I have been removed loool " + name, Toast.LENGTH_SHORT).show();
        group.removeView(view);
      }
    });
    return chip;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    imageUploadHelper.collectCroppedImage(requestCode, resultCode, data, new ImageUploadHelper.CroppingImageCallback() {
      @Override
      public void getCroppedImage(Uri uri) {
        imageUploadHelper.compressImage(uri, new ImageUploadHelper.CompressedImageCallback() {
          @Override
          public void getCompressedImage(Bitmap compressedBitmap) {
            image.setImageBitmap(compressedBitmap);
          }
        });
      }

      @Override
      public void getCroppingError(Exception e) {
        Toast.makeText(TestActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });

  }


}
