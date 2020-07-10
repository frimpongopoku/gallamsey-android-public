package com.pongo.zembe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestActivity extends AppCompatActivity {

  CircleImageView image;
  Button btn;
  ImageUploadHelper imageUploadHelper;
  Activity activity;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    btn = findViewById(R.id.pic_btn);
    image = findViewById(R.id.image);
    activity = this;
    imageUploadHelper = new ImageUploadHelper(this);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
       imageUploadHelper.openFileChooserWithCropper(activity,1,1);
      }
    });

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
        Toast.makeText(TestActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });

  }









}
