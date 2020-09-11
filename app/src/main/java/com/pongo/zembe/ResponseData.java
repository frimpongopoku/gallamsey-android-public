package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResponseData {
  JSONArray content = new JSONArray();
  Gson gson = new Gson();


  public ResponseData() {
  }

  public JSONArray getContent() {
    return content;
  }

  public void setContent(JSONArray content) {
    this.content = content;
  }



  @Override
  public String toString() {
    return "ResponseData{" +
      "content=" + content +
      '}';
  }

}
