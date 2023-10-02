package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.entity.embed.IdentityObjectId;
import com.ncsgroup.ems.entity.identity.IdentityObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdentityObjectRepository extends JpaRepository<IdentityObject, IdentityObjectId> {
  @Query("delete from IdentityObject io where io.id.vehicleId = :vehicleId")
  @Modifying
  void delete(long vehicleId);

  @Query("SELECT COUNT(io) > 0 FROM IdentityObject io WHERE io.id.vehicleId = :vehicleId AND io.id.personId = :personId")
  boolean existsById(Long vehicleId, Long personId);

  @Modifying
  @Query("update IdentityObject io set io.id.personId = :personId")
  void update(Long personId);

  @Query("select io from IdentityObject io where io.id.vehicleId = :vehicleId")
  Optional<IdentityObject> findByVehicleId(Long vehicleId);

  @Query(value = "INSERT INTO identity_object values(:vehicleId, :personId, :type)", nativeQuery = true)
  @Modifying
  @Transactional
  void insert(Long vehicleId, Long personId, Integer type);

}
