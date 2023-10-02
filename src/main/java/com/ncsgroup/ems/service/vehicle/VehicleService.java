package com.ncsgroup.ems.service.vehicle;

import com.ncsgroup.ems.dto.request.vehicle.VehicleFilter;
import com.ncsgroup.ems.dto.request.vehicle.VehicleRequest;
import com.ncsgroup.ems.dto.request.vehicle.VehicleSearchRequest;
import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import com.ncsgroup.ems.dto.response.vehicle.*;
import com.ncsgroup.ems.entity.vehicle.Vehicle;
import com.ncsgroup.ems.service.base.BaseService;

import java.util.List;

public interface VehicleService extends BaseService<Vehicle> {

  VehicleResponse create(
        Long brandId,
        Long typeId,
        String licensePlate,
        Long vehicleCardId,
        List<Long> colorIds
  );

  VehicleResponse detail(Long id);


  void remove(long id);

  VehiclePageResponse list(VehicleFilter vehicleFilter, int size, int page, boolean isAll);

//  VehicleDetail detail(Long id);

  Long getVehicleCardId(Long idVehicle);

  VehicleLicensePlatePageResponse listLicensePlate(String keyword, int size, int page, boolean isAll);

  VehicleResponse update(Long id, VehicleRequest request, Long vehicleCardId);

  void checkNotFound(List<Long> id);

  List<String> findLicensePlates(List<Long> vehicleIds);

  List<IdentityResponse> getByGroupId(Long groupId);

  List<Long> getByPersonId(long personId);

  List<IdentityResponse> getByGroupIds(List<Long> groupIds);

  List<IdentityResponse> getByOutsideGroupIds(List<Long> groupIds);

  List<IdentityResponse> getByPersonId(Long personId);

  List<VehicleResponse> getByGroupId(VehicleSearchRequest request);
}
