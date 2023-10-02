package com.ncsgroup.ems.service.base;


import com.ncsgroup.ems.repository.base.BaseRepository;

import java.util.List;

public class BaseServiceImpl<T> implements BaseService<T> {
  private final BaseRepository<T> repository;

  public BaseServiceImpl(BaseRepository<T> repository) {
    this.repository = repository;
  }

  @Override
  public T create(T t) {
    return repository.save(t);
  }

  @Override
  public T update(T t) {
    return repository.save(t);
  }

  @Override
  public void delete(Long id) {repository.deleteById(id);}

  @Override
  public T get(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<T> list() {return repository.findAll();}
}
