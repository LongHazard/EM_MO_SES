package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.vehicle.brand.VehicleBrandResponse;
import com.ncsgroup.ems.entity.vehicle.VehicleBrand;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleBrandRepository extends BaseRepository<VehicleBrand> {
  boolean existsByName(String name);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.vehicle.brand.VehicleBrandResponse ( " +
        " vb.id, vb.name ) " +
        "FROM VehicleBrand  vb where :keyword is null or lower(vb.name) like %:keyword%")
  List<VehicleBrandResponse> listBySearch(String keyword);

}
