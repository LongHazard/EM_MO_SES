package com.ncsgroup.ems.service.identity;

import com.ncsgroup.ems.entity.identity.IdentityObjectGroup;
import com.ncsgroup.ems.service.base.BaseService;

import java.util.List;

public interface IdentityGroupObjectService extends BaseService<IdentityObjectGroup> {
  void update(Long vehicleId, List<Long> groupIds);
}
