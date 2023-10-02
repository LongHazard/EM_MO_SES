package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.entity.vehicle.VehicleCardColor;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleCardColorRepository extends BaseRepository<VehicleCardColor> {
  @Query(value = "select vcc.color_id from vehicle_card_color vcc where vcc.vehicle_card_id = ?", nativeQuery = true)
  List<Long> findByVehicleCardId(Long vehicleCardId);

  @Modifying
  @Query(value = "delete from vehicle_card_color vcc where vcc.vehicle_card_id = :vehicleCardId ", nativeQuery = true)
  void deleteByVehicleCardId(Long vehicleCardId);

  boolean existsById_VehicleCardId(Long vehicleCardId);

  List<VehicleCardColor> getVehicleCardColorsById_VehicleCardId(Long vehicleCardId);
}
