package com.ncsgroup.ems.service.address.impl;

import com.ncsgroup.ems.dto.request.address.SearchWardRequest;
import com.ncsgroup.ems.dto.response.address.ward.WardPageResponse;
import com.ncsgroup.ems.entity.address.Ward;
import com.ncsgroup.ems.repository.WardRepository;
import com.ncsgroup.ems.service.address.WardService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
public class WardServiceImpl extends BaseServiceImpl<Ward> implements WardService {

  private final WardRepository repository;

  public WardServiceImpl(WardRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public WardPageResponse search(SearchWardRequest request, int page, int size) {
    log.info("(search) request: {}, page:{}, size:{}", request, page, size);

    String keyword = (request == null || request.getKeyword() == null) ? null : request.getKeyword().toLowerCase();
    String districtCode = (request == null) ? null : request.getDistrictCode();
    Pageable pageable = PageRequest.of(page, size);

    return new WardPageResponse(
          repository.search(keyword, districtCode, pageable),
          repository.countSearch(keyword, districtCode)
    );
  }
}
