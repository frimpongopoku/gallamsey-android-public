package com.pongo.zembe;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;

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


  public void compressImage(Uri uri, final CompressedImageCallback callback) {
    new BackgroundCompressor(context.getContentResolver(), new CompressedImageCallback() {
      @Override
      public void getCompressedImage(byte[] compressedImageInBytesArray) {
        callback.getCompressedImage(compressedImageInBytesArray);
      }
    }).execute(uri);
  }

  public void openFileChooser(FileChooserCallback fileChooserCallback) {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    fileChooserCallback.getBackChooserIntent(intent);
  }

  public interface CompressedImageCallback {
    void getCompressedImage(byte[] compressedImageInBytesArray);
  }

  public interface FileChooserCallback {
    /**
     * returns the image file picker intent
     * Just call "startActivityForResult" with this intent and be on your way
     */
    void getBackChooserIntent(Intent intent);
  }


  public class BackgroundCompressor extends AsyncTask<Uri,Integer, byte[]>{
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
    protected byte[] doInBackground(Uri... uris) {
      Log.w("resizing:", "started resizing in background");
      try {
        bitmap = MediaStore.Images.Media.getBitmap(resolver,uris[0]);

        Log.d("beforeCompression",String.valueOf(bitmap.getAllocationByteCount()));
//        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(resolver, uris[0]));
      } catch (Exception e) {
        Log.w("errorOnResizing", e.getMessage());
      }
      byte[] bytes =  getBytesFromBitmap(bitmap,80);
      Log.d("afterCompression",String.valueOf(bytes.length));
      return bytes;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
      super.onPostExecute(bytes);
      imageCallback.getCompressedImage(bytes);
    }

  }

}

