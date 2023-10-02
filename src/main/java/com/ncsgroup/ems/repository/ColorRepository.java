package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.color.ColorResponse;
import com.ncsgroup.ems.entity.vehicle.Color;

import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends BaseRepository<Color> {
  boolean existsByName(String name);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.color.ColorResponse(c.id, c.name) " +
        "FROM Color c where :keyword is null " +
        "or lower(c.name) like %:keyword%")
  List<ColorResponse> search(String keyword);

  @Query(value = "SELECT c.name FROM vehicles v JOIN vehicle_color vc ON v.id = vc.vehicle_id JOIN colors c ON c.id = vc.color_id WHERE v.id = ?1", nativeQuery = true)
  List<String> findNameColor(Long idVehicle);

  @Query(value = "SELECT c.name FROM  colors c  WHERE c.id IN :colorIds", nativeQuery = true)
  List<String> findNameColorById(List<Long> colorIds);

  List<Color> findByIdIn(List<Long> colorIds);

  @Query(value = "select count(*) from colors c where c.id IN :colorIds", nativeQuery = true)
  Integer checkNotFound(List<Long> colorIds);

  @Query("select c.name " +
        "from Color c join VehicleColor vc on c.id = vc.id.colorId " +
        "join Vehicle v on vc.id.vehicleId = v.id " +
        "where v.id  =:vehicleId")
  List<String> find(Long vehicleId);
}
