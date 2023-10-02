package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO;
import com.ncsgroup.ems.entity.vehicle.VehicleType;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleTypeRepository extends BaseRepository<VehicleType> {
  boolean existsByName(String name);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO(vt.id, vt.name)" +
        "        FROM VehicleType vt where :keyword is null " +
        "        or lower(vt.name) like %:keyword%")
  List<VehicleTypeResponseDTO> search(String keyword);


}
