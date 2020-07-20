package com.pongo.zembe;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateHelper {

  public static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:SS");

  public static String getTimeAgo(String date) {
    Date dateTo = convertStringToDate(date);
    Date now = convertStringToDate(getDateInMyTimezone());
    return (String) DateUtils.getRelativeTimeSpanString(dateTo.getTime(), now.getTime(), DateUtils.MINUTE_IN_MILLIS);
  }

  public static String getTimezone() {
    Calendar cal = Calendar.getInstance();
    long milliDiff = cal.get(Calendar.ZONE_OFFSET);
    // Get local offset, now loop through available timezone id(s).
    String[] ids = TimeZone.getAvailableIDs();
    String name = null;
    for (String id : ids) {
      TimeZone tz = TimeZone.getTimeZone(id);
      if (tz.getRawOffset() == milliDiff) {
        // Found a match.
        name = id;
        break;
      }
    }
    return name;
  }

  public static int fromHoursToMinutes(double value) {
    double mins = value * 60;
    return (int) mins;
  }

  public static String jumpDateByHours(String date, double howManyHours) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(convertStringToDate(date));
    if (howManyHours < 1.0) {
      // means we are dealing with minutes and not hours
      int mins = fromHoursToMinutes(howManyHours);
      cal.add(cal.MINUTE, mins);
      return formatter.format(cal.getTime());

    } else {
      int hours = (int) howManyHours;
      cal.add(cal.HOUR_OF_DAY, hours);
      return formatter.format(cal.getTime());
    }
  }

  public static Date convertStringToDate(String stringDate) {
    try {
      Date date = formatter.parse(stringDate);
      return date;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getDateInMyTimezone() {
    Date date = new Date();
    formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
    return formatter.format(date);
  }

  public static long getMilliSecondsFromDate(String dateString) {
    try {
      Calendar cal = Calendar.getInstance();
      Date date = formatter.parse(dateString);
      cal.setTime(date);
      return cal.getTimeInMillis();

    } catch (Exception e) {
      Log.w("errorGettingMilliTime", e.getMessage());

    }
    return 0;
  }

  public static double getHoursValueFromDurationString(String timeDuration) {
    switch (timeDuration) {
      case Konstants.THIRTY_MINUTES: {
        return 0.5;

      }
      case Konstants.ONE_HOUR: {
        return 1;

      }
      case Konstants.TWO_HOURS: {
        return 2;

      }
      case Konstants.THREE_HOURS: {
        return 3;
      }
      case Konstants.FOUR_HOURS: {
        return 4;
      }
      case Konstants.FIVE_HOURS: {
        return 5;
      }
      case Konstants.SIX_HOURS: {
        return 6;
      }
      case Konstants.ONE_DAY: {
        return 24;
      }
      case Konstants.TWO_DAYS: {
        return 48;
      }
    }
    return 0;
  }


}
