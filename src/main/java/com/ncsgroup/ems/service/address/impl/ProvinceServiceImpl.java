package com.ncsgroup.ems.service.address.impl;

import com.ncsgroup.ems.dto.response.address.province.ProvincePageResponse;
import com.ncsgroup.ems.entity.address.Province;
import com.ncsgroup.ems.repository.ProvinceRepository;
import com.ncsgroup.ems.service.address.ProvinceService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
public class ProvinceServiceImpl extends BaseServiceImpl<Province> implements ProvinceService {
  private final ProvinceRepository repository;

  public ProvinceServiceImpl(ProvinceRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public ProvincePageResponse search(String keyword, int page, int size) {
    log.info("(search) keyword: {}, page:{}, size:{}", keyword, page, size);

    keyword = (keyword == null) ? null : keyword.toLowerCase();
    Pageable pageable = PageRequest.of(page, size);

    return new ProvincePageResponse(
          repository.search(keyword, pageable),
          repository.countSearch(keyword)
    );
  }
}
