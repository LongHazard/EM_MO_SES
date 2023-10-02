package com.ncsgroup.ems.service.address.impl;

import com.ncsgroup.ems.dto.request.address.SearchDistrictRequest;
import com.ncsgroup.ems.dto.response.address.district.DistrictPageResponse;
import com.ncsgroup.ems.entity.address.District;
import com.ncsgroup.ems.repository.DistrictRepository;
import com.ncsgroup.ems.service.address.DistrictService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
public class DistrictServiceImpl extends BaseServiceImpl<District> implements DistrictService {

  private final DistrictRepository repository;

  public DistrictServiceImpl(DistrictRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public DistrictPageResponse search(SearchDistrictRequest request, int page, int size) {
    log.info("(search) request: {}, page:{}, size:{}", request, page, size);

    String keyword = (request == null || request.getKeyword() == null) ? null : request.getKeyword().toLowerCase();
    String provinceCode = (request == null) ? null : request.getProvinceCode();
    Pageable pageable = PageRequest.of(page, size);

    return new DistrictPageResponse(
          repository.search(keyword, provinceCode, pageable),
          repository.countSearch(keyword, provinceCode)
    );
  }
}
