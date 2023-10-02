package com.ncsgroup.ems.service.vehicle.impl;

import com.ncsgroup.ems.entity.vehicle.VehicleCardColor;
import com.ncsgroup.ems.entity.vehicle.VehicleColor;
import com.ncsgroup.ems.repository.VehicleCardColorRepository;
import com.ncsgroup.ems.service.vehicle.VehicleCardColorService;
import com.ncsgroup.ems.service.base.BaseServiceImpl;
import com.ncsgroup.ems.utils.ComparatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
public class VehicleCardColorServiceImpl extends BaseServiceImpl<VehicleCardColor> implements VehicleCardColorService {
  private final VehicleCardColorRepository repository;

  public VehicleCardColorServiceImpl(VehicleCardColorRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  @Transactional
  public void update(Long vehicleCardId, List<Long> colorIds) {
    log.info("(update) vehicleCardId: {}, colorIds: {}", vehicleCardId, colorIds);
    if (Objects.isNull(colorIds) || colorIds.isEmpty()) return;

    List<VehicleCardColor> existedVehicleCardColors = repository.getVehicleCardColorsById_VehicleCardId(vehicleCardId);
    List<VehicleCardColor> updateVehicleCardColors = colorIds.stream()
          .map(colorId -> VehicleCardColor.of(
                vehicleCardId,
                colorId
          ))
          .toList();

    repository.deleteAll(ComparatorUtils.findUniqueElements(existedVehicleCardColors, updateVehicleCardColors));
    repository.saveAll(ComparatorUtils.findUniqueElements(updateVehicleCardColors, existedVehicleCardColors));
  }


}
