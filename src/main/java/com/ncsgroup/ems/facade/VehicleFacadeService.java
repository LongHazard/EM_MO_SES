package com.ncsgroup.ems.facade;

import com.ncsgroup.ems.dto.request.vehicle.FacadeVehicleRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleFacadeRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleFilter;
import com.ncsgroup.ems.dto.response.vehicle.*;

import java.util.List;

public interface VehicleFacadeService {
  VehicleFacadeResponse createVehicle(VehicleFacadeRequest request);

  FacadeVehicleResponse createVehicleWithMultiPerson(FacadeVehicleRequest request);

  VehiclePageResponse list(VehicleFilter vehicleFilter, int size, int page, boolean isAll);

  VehicleFacadeResponse detail(Long id);

  VehicleFacadeResponse update(Long id, VehicleFacadeRequest request);

  List<VehicleFacadeResponse> getByPersonId(long personId);



}
