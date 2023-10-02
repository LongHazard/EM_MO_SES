package com.ncsgroup.ems.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateUtils {
  public static String getCurrentDateString() {
    return LocalDate.now().toString();
  }

  public static Long currentTimeMillis() {
    return System.currentTimeMillis();
  }

  public static long getMillisSecond() {
    return Calendar.getInstance().getTimeInMillis();
  }

  public static Long convertToTimestamp(String date) {
    if (Objects.isNull(date)) return null;
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      Date parsedDate = dateFormat.parse(date);
      return parsedDate.getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String convertTimestampToDate(long timestamp) {
    if (Objects.isNull(timestamp)) return null;
    Date date = new Date(timestamp);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    return sdf.format(date);
  }

}
