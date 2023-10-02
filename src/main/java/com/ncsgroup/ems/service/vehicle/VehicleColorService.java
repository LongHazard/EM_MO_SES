package com.ncsgroup.ems.service.vehicle;

import com.ncsgroup.ems.entity.vehicle.VehicleColor;
import com.ncsgroup.ems.service.base.BaseService;

import java.util.List;

public interface VehicleColorService extends BaseService<VehicleColor> {
  void create(Long vehicleId, List<Long> colorId);

  void delete(Long vehicleId);

  void update(Long vehicleId, List<Long> colorId);

  List<Long> getColorIdsByVehicleId(Long vehicleId);
}
