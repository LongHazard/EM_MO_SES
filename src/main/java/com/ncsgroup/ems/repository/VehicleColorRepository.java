package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.entity.embed.VehicleColorId;
import com.ncsgroup.ems.entity.vehicle.VehicleColor;
import com.ncsgroup.ems.repository.base.BaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleColorRepository extends BaseRepository<VehicleColor> {
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM vehicle_color WHERE vehicle_id = ?1", nativeQuery = true)
  void delete(Long idVehicle);

  @Query("select vc from VehicleColor vc where vc.id.vehicleId = :vehicleId")
  List<VehicleColor> getByVehicleId(Long vehicleId);

  @Query("select vc.id.colorId from VehicleColor vc where vc.id.vehicleId = :vehicleId")
  List<Long> getColorIdsByVehicleId(Long vehicleId);
}
