package com.ncsgroup.ems.service.vehicle;

import com.ncsgroup.ems.dto.request.vehicle.type.VehicleTypeRequest;
import com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO;
import com.ncsgroup.ems.entity.vehicle.VehicleType;
import com.ncsgroup.ems.service.base.BaseService;

import java.util.List;

public interface VehicleTypeService extends BaseService<VehicleType> {
  VehicleTypeResponseDTO create(VehicleTypeRequest request);

  void delete(Long id);

  List<VehicleTypeResponseDTO> list(String keyword);

  VehicleType find(Long id);

  VehicleTypeResponseDTO detail(Long id);

  //  Long checkTypeVehicleNotFound(Long id);
  void checkNameExist(String name);
}
