package com.pongo.zembe;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Just a simple class used to model any JSON response object that's been returned from the backend
 */
public class ResponseHandler {


  public Gson gson = new Gson();
  private Returnables returnables = new Returnables();
  private GalResponseError error = new GalResponseError();
  private ResponseData data = new ResponseData();

  public ResponseHandler() {
  }


  public static ResponseHandler newInstance(JSONObject response) {
    Gson gson = new Gson();
    ResponseHandler handler = new ResponseHandler();
    GalResponseError responseError;
    Returnables returnables = new Returnables();
    ResponseData responseData = new ResponseData();
    try {
      String error = response.get("error").toString();
      responseError = gson.fromJson(error, GalResponseError.class);
      JSONObject returnablesObj = (JSONObject) response.get("returnables");
      if (returnablesObj != null) {
        returnables = gson.fromJson(returnablesObj.toString(), Returnables.class);
      }
      JSONArray content = (JSONArray) response.get("data");
      responseData.setContent(content);
      handler.setError(responseError);
      handler.setData(responseData);
      handler.setReturnables(returnables);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return handler;
  }

  @Override
  public String toString() {
    return "ResponseHandler{" +
      ", returnables=" + returnables +
      ", error=" + error +
      ", data=" + data +
      '}';
  }

  public void setReturnables(Returnables returnables) {
    this.returnables = returnables;
  }

  public void setError(GalResponseError error) {
    this.error = error;
  }

  public void setData(ResponseData data) {
    this.data = data;
  }


}