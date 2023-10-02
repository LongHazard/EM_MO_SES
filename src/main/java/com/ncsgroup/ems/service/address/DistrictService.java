package com.ncsgroup.ems.service.address;

import com.ncsgroup.ems.dto.request.address.SearchDistrictRequest;
import com.ncsgroup.ems.dto.response.address.district.DistrictPageResponse;
import com.ncsgroup.ems.entity.address.District;
import com.ncsgroup.ems.service.base.BaseService;

public interface DistrictService extends BaseService<District> {

  DistrictPageResponse search(SearchDistrictRequest request, int page, int size);
}
