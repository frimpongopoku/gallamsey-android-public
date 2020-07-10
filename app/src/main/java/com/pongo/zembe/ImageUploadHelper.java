package com.pongo.zembe;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * In case you forget to brind in some libraries for this helper, links are down below
 * https://github.com/ArthurHub/Android-Image-Cropper
 */
public class ImageUploadHelper {
  private Context context;

  public ImageUploadHelper(Context context) {
    this.context = context;
  }

  public String getFileExtension(Uri uri) {
    ContentResolver cr = context.getContentResolver();
    MimeTypeMap mime = MimeTypeMap.getSingleton();
    return mime.getExtensionFromMimeType(cr.getType(uri));
  }


  public void openFileChooserWithCropper(Activity activity, int aspectRationX, int aspectRatioY) {
    CropImage.activity()
      .setGuidelines(CropImageView.Guidelines.ON)
      .setAspectRatio(aspectRationX, aspectRatioY)
      .start(activity);
  }

  public void collectCroppedImage(int requestCode, int resultCode, @Nullable Intent data, CroppingImageCallback croppingResult) {
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      if (resultCode == RESULT_OK) {
        Uri resultUri = result.getUri();
        croppingResult.getCroppedImage(result.getUri());
      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        Exception error = result.getError();
        croppingResult.getCroppingError(error);
      }
    }

  }

  public void compressImage(Uri uri, final CompressedImageCallback callback) {
    new BackgroundCompressor(context.getContentResolver(), new CompressedImageCallback() {
      @Override
      public void getCompressedImage(Bitmap compressedBitmap) {
        callback.getCompressedImage(compressedBitmap);
      }
    }).execute(uri);
  }


  public void openFileChooser(FileChooserCallback fileChooserCallback) {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    fileChooserCallback.getBackChooserIntent(intent);
  }

  public interface CroppingImageCallback {
    void getCroppedImage(Uri uri);

    void getCroppingError(Exception e);
  }

  public interface CompressedImageCallback {
    void getCompressedImage(Bitmap compressedBitmap);
  }

  public interface FileChooserCallback {
    /**
     * returns the image file picker intent
     * Just call "startActivityForResult" with this intent and be on your way
     */
    void getBackChooserIntent(Intent intent);
  }


  public class BackgroundCompressor extends AsyncTask<Uri, Integer, Bitmap> {
    ContentResolver resolver;
    Bitmap bitmap;
    CompressedImageCallback imageCallback;

    public BackgroundCompressor(ContentResolver resolver, CompressedImageCallback imageCallback) {
      this.resolver = resolver;
      this.imageCallback = imageCallback;
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap, Integer quality) {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
      return stream.toByteArray();
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Uri... uris) {
      try {
        bitmap = MediaStore.Images.Media.getBitmap(resolver, uris[0]);
      } catch (Exception e) {
        Log.w("errorOnResizing", e.getMessage());
      }
      byte[] bytesArr = getBytesFromBitmap(bitmap, 60);
      Bitmap bitmap = BitmapFactory.decodeByteArray(bytesArr, 0, bytesArr.length);
      return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
      super.onPostExecute(bitmap);
      imageCallback.getCompressedImage(bitmap);
    }

  }

}

