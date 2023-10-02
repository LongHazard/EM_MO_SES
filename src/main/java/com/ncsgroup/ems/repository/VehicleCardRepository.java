package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.entity.vehicle.VehicleCard;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VehicleCardRepository extends BaseRepository<VehicleCard> {
  boolean existsByCardCode(String cardCode);

  boolean existsByLicensePlate(String licensePlate);

  @Query("select vc from VehicleCard vc")
  List<VehicleCard> getAll(Pageable pageable);

  @Transactional
  @Modifying
  @Query(value = "update VehicleCard  vc set vc.permanentResident = :addressId where vc.id = :id")
  void updatePermanentResident(long addressId, long id);
}
