package com.ncsgroup.ems.service.vehicle;

import com.ncsgroup.ems.dto.request.vehicle.brand.VehicleBrandRequest;
import com.ncsgroup.ems.dto.response.vehicle.brand.VehicleBrandResponse;
import com.ncsgroup.ems.entity.vehicle.VehicleBrand;
import com.ncsgroup.ems.service.base.BaseService;

import java.util.List;

public interface VehicleBrandService extends BaseService<VehicleBrand> {

  /**
   * Creates a new vehicle brand based on the provided request.
   *
   * @param request The vehicle brand request containing the necessary data.
   * @return The response containing the created vehicle brand information.
   */
  VehicleBrandResponse create(VehicleBrandRequest request);

  /**
   * Retrieves a list of vehicle brands based on the provided keyword.
   *
   * @param keyword The keyword to search for in the vehicle brand names.
   * @return A list of vehicle brand responses matching the provided keyword.
   */
  List<VehicleBrandResponse> list(String keyword);

  /**
   * Removes a vehicle brand with the specified ID.
   *
   * @param id The ID of the vehicle brand to be removed.
   */
  void remove(Long id);

  VehicleBrand find(Long id);

  VehicleBrandResponse detail(Long id);

//  Long checkBrandVehicleNotFound(Long id);

}