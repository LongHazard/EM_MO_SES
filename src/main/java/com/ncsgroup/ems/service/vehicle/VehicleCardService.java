package com.ncsgroup.ems.service.vehicle;

import com.ncsgroup.ems.dto.request.vehicle.card.VehicleCardRequest;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardPageResponse;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleCardResponse;
import com.ncsgroup.ems.entity.vehicle.VehicleCard;
import com.ncsgroup.ems.service.base.BaseService;

import java.util.List;

public interface VehicleCardService extends BaseService<VehicleCard> {
  VehicleCardResponse create(VehicleCardRequest request);

  VehicleCard find(Long id);

  VehicleCard findOrDefault(Long id);

  List<Long> findColorIds(Long vehicleCardId);

  VehicleCardPageResponse list(int size, int page, boolean isAll);

  VehicleCardResponse update(Long id, VehicleCardRequest request);

  void checkVehicleCardCodeExist(String cardCode);

  void checkLicensePlateExist(String licensePlate);

  void updatePermanentResident(long addressId, long vehicleCardId);

  void saveVehicleCardColor(Long vehicleCardId, List<Long> colorIds);
}
