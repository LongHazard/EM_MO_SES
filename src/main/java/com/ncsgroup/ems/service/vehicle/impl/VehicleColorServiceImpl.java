package com.ncsgroup.ems.service.vehicle.impl;

import com.ncsgroup.ems.entity.vehicle.VehicleColor;
import com.ncsgroup.ems.repository.VehicleColorRepository;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import com.ncsgroup.ems.service.vehicle.VehicleColorService;
import com.ncsgroup.ems.utils.ComparatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class VehicleColorServiceImpl extends BaseServiceImpl<VehicleColor> implements VehicleColorService {

  private final VehicleColorRepository repository;

  public VehicleColorServiceImpl(VehicleColorRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public void create(Long vehicleId, List<Long> colorIds) {
    log.info("(create) vehicleId: {} colorIds: {}", vehicleId, colorIds);
    List<VehicleColor> vehicleColors = new ArrayList<>();
    for (Long colorId : colorIds) {
      vehicleColors.add(VehicleColor.of(vehicleId, colorId));
    }
    repository.saveAll(vehicleColors);
  }

  @Override
  public void delete(Long vehicleId) {
    log.info("(delete) vehicleId: {}", vehicleId);
    repository.delete(vehicleId);
  }

  @Override
  @Transactional
  public void update(Long vehicleId, List<Long> colorIds) {
    log.info("(update)vehicleId: {}, colorIds: {}", vehicleId, colorIds);

    if (Objects.isNull(colorIds) || colorIds.isEmpty()) {
      repository.deleteAll();
      return;
    }


    List<VehicleColor> existedVehicleColors = repository.getByVehicleId(vehicleId);

    List<VehicleColor> updateVehicleColors = colorIds.stream()
          .map(colorId -> VehicleColor.of(vehicleId, colorId))
          .toList();

    repository.deleteAll(ComparatorUtils.findUniqueElements(existedVehicleColors, updateVehicleColors));
    repository.saveAll(ComparatorUtils.findUniqueElements(updateVehicleColors, existedVehicleColors));
  }


  @Override
  public List<Long> getColorIdsByVehicleId(Long vehicleId) {
    log.info("(getColorIdsByVehicleId) vehicleId: {}", vehicleId);
    return repository.getColorIdsByVehicleId(vehicleId);
  }
}
