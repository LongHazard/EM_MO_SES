package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.entity.identity.IdentityObjectGroup;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IdentityObjectGroupRepository extends BaseRepository<IdentityObjectGroup> {

  @Query(value = "insert into identity_object_group(person_id,group_id) values (:personId, :groupId)", nativeQuery = true)
  @Modifying
  @Transactional
  void save(long personId, List<Long> groupId);

  @Modifying
  @Transactional
  @Query(value = "DELETE From identity_object_group io where io.vehicle_id = ?1 ", nativeQuery = true)
  void deleteByVehicle(Long vehicleId);

  void deleteByPersonId(Long personId);

  @Modifying
  @Transactional
  @Query(value = "DELETE From identity_object_group io where io.vehicle_id = ?1 ", nativeQuery = true)
  void delete(Long vehicleId);


  @Query("select iog from IdentityObjectGroup iog where iog.vehicleId = :vehicleId")
  List<IdentityObjectGroup> getByVehicleId(Long vehicleId);

  void deleteAllByVehicleId(Long vehicleId);
}
