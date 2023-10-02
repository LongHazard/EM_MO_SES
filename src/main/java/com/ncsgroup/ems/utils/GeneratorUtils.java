package com.ncsgroup.ems.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GeneratorUtils {
  private static final String SIX_DIGITS_STRING = "000000";
  private static final Integer SIX_DIGITS_UPPER_BOUND = 1000000;

  public static String generateOtp() {
    Random random = new Random();
    return new DecimalFormat(SIX_DIGITS_STRING).format(random.nextInt(SIX_DIGITS_UPPER_BOUND));
  }

  public static String generateFileName(String filename) {
    return String.valueOf(DateUtils.getMillisSecond()).concat(filename);
  }

  public static String generateUid(){
    return UUID.randomUUID().toString();
  }

  public static void assignNullIfEmpty(String value) {
    if (value == null || value.isEmpty()) {
      value = null;
    }
  }

  public static  <T> List<T> convertNullToListLong(List<T> value) {
    if (value == null) {
      return new ArrayList<>();
    } else {
      return value;
    }
  }

  public static  List<String> convertNullToListString(List<String> value) {
    if (value == null) {
      return new ArrayList<>();
    }
    return value;
  }


}
