package com.ncsgroup.ems.utils;

import java.util.*;

public class ComparatorUtils {
  public static void main(String[] args) {
    List<Integer> arr1 = Arrays.asList(1,2,3,4,5);
    List<Integer> arr2 = Arrays.asList(3,4,5);
    System.out.println(findUniqueElements(arr1,arr2));
  }
  public static <T> List<T> findDuplicates(List<T> list1, List<T> list2) {
    Set<T> set = new HashSet<>(list1);
    List<T> duplicates = new ArrayList<>();

    for (T element : list2) {
      if (set.contains(element)) {
        duplicates.add(element);
      }
    }

    return duplicates;
  }

  public static <T> List<T> findUniqueElements(List<T> list1, List<T> list2) {
    Set<T> set = new HashSet<>(list2);
    List<T> uniqueElements = new ArrayList<>();

    for (T element : list1) {
      if (!set.contains(element)) {
        uniqueElements.add(element);
      }
    }

    return uniqueElements;
  }
}
