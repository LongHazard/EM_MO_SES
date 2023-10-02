package com.ncsgroup.ems.service.address;

import com.ncsgroup.ems.dto.request.address.SearchWardRequest;
import com.ncsgroup.ems.dto.response.address.ward.WardPageResponse;
import com.ncsgroup.ems.entity.address.Ward;
import com.ncsgroup.ems.service.base.BaseService;

public interface WardService extends BaseService<Ward> {

  WardPageResponse search(SearchWardRequest request, int page, int size);
}
