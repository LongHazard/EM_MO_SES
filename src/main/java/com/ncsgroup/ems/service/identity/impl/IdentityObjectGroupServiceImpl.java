package com.ncsgroup.ems.service.identity.impl;

import com.ncsgroup.ems.entity.identity.IdentityObjectGroup;
import com.ncsgroup.ems.entity.vehicle.VehicleColor;
import com.ncsgroup.ems.repository.IdentityObjectGroupRepository;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import com.ncsgroup.ems.service.identity.IdentityGroupObjectService;
import com.ncsgroup.ems.utils.ComparatorUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public class IdentityObjectGroupServiceImpl extends BaseServiceImpl<IdentityObjectGroup> implements IdentityGroupObjectService {
  private final IdentityObjectGroupRepository repository;

  public IdentityObjectGroupServiceImpl(IdentityObjectGroupRepository repository) {
    super(repository);
    this.repository = repository;
  }


  @Override
  public void update(Long vehicleId, List<Long> groupIds) {
    log.info("(update)vehicleId: {}, groupIds: {}", vehicleId, groupIds);

    if (Objects.isNull(groupIds) || groupIds.isEmpty()) {
      repository.deleteAllByVehicleId(vehicleId);
      return;
    }

    List<IdentityObjectGroup> updateIdentityObjectGroups = groupIds.stream()
          .map(groupId -> IdentityObjectGroup.of(groupId, vehicleId))
          .toList();

    repository.deleteAllByVehicleId(vehicleId);
    repository.saveAll(updateIdentityObjectGroups);
  }
}
