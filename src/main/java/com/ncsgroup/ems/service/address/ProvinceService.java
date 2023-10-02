package com.ncsgroup.ems.service.address;

import com.ncsgroup.ems.dto.response.address.province.ProvincePageResponse;
import com.ncsgroup.ems.entity.address.Province;
import com.ncsgroup.ems.service.base.BaseService;

public interface ProvinceService extends BaseService<Province> {
  ProvincePageResponse search(String keyword, int page, int size);
}
