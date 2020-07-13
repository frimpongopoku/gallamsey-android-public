package com.pongo.zembe;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RandomHelpersClass {


  public static String concactToWhat(String motherString, String tobeAttached) {
    if (motherString.trim().equals("")) {
      motherString = motherString + tobeAttached + " ";
    } else {
      motherString = motherString + "\n" + tobeAttached;
    }

    return motherString;
  }

  public static Chip createChip(Context context, final String name, final GalInterfaceGuru.TagDialogChipActions tagDialogChipActions) {
    Chip chip = new Chip(context);
    chip.setText(name);
    chip.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        tagDialogChipActions.removeTag(view);
      }
    });
    return chip;
  }

  public static String mergeTextsFromArray(ArrayList<String> arr) {
    String finalR = "";
    for (int i = 0; i < arr.size(); i++) {
      finalR = finalR.equals("") ? arr.get(i) : finalR + ", " + arr.get(i);
    }
    return finalR;
  }

  public static int countChar(String string) {
    int count = 0;
    for (int i = 0; i < string.length(); i++) {
      count++;
    }

    return count;
  }

  public static boolean validateDay(int day) {
    if (day > 31 || day <= 0) {
      return false;
    }
    return true;
  }

  public static boolean validateMonth(int month) {
    if (month <= 0 || month >= 12) {
      return false;
    }
    return true;
  }

  public static boolean validateYear(int year) {
    //the year should be in a full four digit code
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    Log.w("Check yearhere:::", String.valueOf(countChar(String.valueOf(year))));
    if (year <= 0 || year >= currentYear - 12 || countChar(String.valueOf(year)) != 4) {
      //No zero years, age limit = 13
      return false;
    }
    return true;
  }

  /**
   * --------------------------------------------------------------------------
   * 1. Check if date is valid. Validation? it must look like this (22-03-1998)
   * 2. Then check if day isnt bigger than 31 or equal to 0
   * 3. Check if month isnt bigger than 12 or equal to 0
   * 4. Then check if year is 4 digits, and if the user is 13 years or older
   * --------------------------------------------------------------------------
   */

  public static HashMap<String, Object> validateDOB(String date) {
    HashMap<String, Object> finalResult = new HashMap<>();
    String[] dateArr = date.split("-");
    ArrayList<String> errorList = new ArrayList<>();
    if (dateArr.length != 3) {
      errorList.add("Invalid date format");
      finalResult.put("status", false);
      finalResult.put("errors", errorList);
      return finalResult;
    }
    boolean goodDay = false, goodMonth = false, goodYear = false;
    goodDay = validateDay(Integer.parseInt(dateArr[0]));
    goodMonth = validateMonth(Integer.parseInt(dateArr[1]));
    goodYear = validateYear(Integer.parseInt(dateArr[2]));
    if (!goodDay) errorList.add("Your day might be a bit out of range");
    if (!goodMonth) errorList.add("Please check if your month is in range");
    if (!goodYear)
      errorList.add("Invalid year.\nPlease check if year is in range and write them in full (eg. 1996, 2020) or \nYou are 13yrs or older");
    if (goodDay && goodMonth && goodYear) {
      finalResult.put("status", true);
      finalResult.put("errors", errorList);
      return finalResult;
    }
    finalResult.put("status", false);
    finalResult.put("errors", errorList);
    return finalResult;

  }
}
