package com.ncsgroup.ems.service.base;

import java.util.List;

public interface BaseService<T> {
  T create(T t);
  T update(T t);
  void delete(Long id);
  T get(Long id);
  List<T> list();
}
