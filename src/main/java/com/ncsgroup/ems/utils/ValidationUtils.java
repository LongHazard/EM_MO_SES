package com.ncsgroup.ems.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ValidationUtils {
  private ValidationUtils() {
  }

  public static <T> boolean isNull(T object) {
    return Objects.isNull(object);
  }

  public static boolean isNullOrEmpty(String str) {
    return isNull(str) || str.isEmpty();
  }

  public static <T> boolean isNullOrEmpty(T input) {
    if (isNull(input)) {
      return true;
    }
    if (input instanceof String) {
      return ((CharSequence) input).length() == 0;
    }

    if (input instanceof Collection) {
      return ((Collection<?>) input).isEmpty();
    }

    if (input instanceof Map) {
      return ((Map<?, ?>) input).isEmpty();
    }

    if (input.getClass().isArray()) {
      return ((Object[]) input).length == 0;
    }

    return false;
  }

  public static <T> boolean equals(T object1, T object2) {
    return Objects.equals(object1, object2);
  }
}
