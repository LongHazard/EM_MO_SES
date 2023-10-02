package com.ncsgroup.ems.service.vehicle;

import com.ncsgroup.ems.entity.vehicle.VehicleCardColor;
import com.ncsgroup.ems.service.base.BaseService;

import java.util.List;

public interface VehicleCardColorService extends BaseService<VehicleCardColor> {

  void update(Long vehicleCardId, List<Long> colorIds);
}
