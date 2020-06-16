package com.pongo.zembe;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class ImageUploadHelper {

  private Context context;

  public ImageUploadHelper(Context context) {
    this.context = context;
  }

  public  String  getFileExtension(Uri uri){
    ContentResolver cr = context.getContentResolver();
    MimeTypeMap mime = MimeTypeMap.getSingleton();
    return mime.getExtensionFromMimeType(cr.getType(uri));
  }

  public void openFileChooser(FileChooserCallback fileChooserCallback) {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    fileChooserCallback.getBackChooserIntent(intent);
  }

  public interface FileChooserCallback {
    void getBackChooserIntent(Intent intent);
  }

}

