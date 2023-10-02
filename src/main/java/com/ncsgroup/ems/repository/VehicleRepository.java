package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import com.ncsgroup.ems.dto.response.vehicle.VehicleDetail;
import com.ncsgroup.ems.dto.response.vehicle.VehicleLicensePlateResponse;
import com.ncsgroup.ems.dto.response.vehicle.vehiclecard.VehicleFilterDetail;
import com.ncsgroup.ems.entity.vehicle.Vehicle;
import com.ncsgroup.ems.repository.base.BaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends BaseRepository<Vehicle> {

  boolean existsByLicensePlate(String licensePlate);

  @Query(value = "select v.id as Id, v.license_plate as License , vt.\"name\" as NameType, vb.\"name\" as NameBrand, pe.name as NamePerson " +
        "FROM vehicles v " +
        "LEFT JOIN vehicle_brand vb ON v.brand_id = vb.id " +
        "LEFT JOIN vehicle_type vt ON v.type_id = vt.id " +
        "LEFT JOIN identity_object io ON v.id = io.vehicle_id " +
        "LEFT JOIN persons pe ON pe.id = io.person_id WHERE((v.is_deleted = FALSE) " +
        "AND (LOWER(v.license_plate) LIKE %:keyword%) " +
        "AND (v.license_plate IN :licensePlates OR :licensePlates IS NULL) " +
        "AND (vt.id IN :vehicleTypeIds OR :vehicleTypeIds IS NULL)) order by v.id desc", nativeQuery = true)
  List<VehicleFilterDetail> search(
        @Param("keyword") String keyword,
        @Param("vehicleTypeIds") List<Long> vehicleTypeIds,
        @Param("licensePlates") List<String> licensePlates,
        Pageable pageable
  );


  @Query(value = "SELECT COUNT(v) FROM vehicles v WHERE v.id IN :idVehicles", nativeQuery = true)
  Integer getCount(List<Long> idVehicles);

  @Query(value = "select v.id as Id, v.license_plate as License , vt.\"name\" as NameType, vb.\"name\" as NameBrand, pe.name as NamePerson " +
        "FROM vehicles v " +
        "LEFT JOIN vehicle_brand vb ON v.brand_id = vb.id " +
        "LEFT JOIN vehicle_type vt ON v.type_id = vt.id " +
        "LEFT JOIN identity_object io ON v.id = io.vehicle_id " +
        "LEFT JOIN persons pe ON pe.id = io.person_id " +
        "WHERE v.is_deleted = FALSE", nativeQuery = true)
  List<VehicleFilterDetail> getAll();


  @Query(value = "select count(v) " +
        "FROM vehicles v " +
        "LEFT JOIN vehicle_brand vb ON v.brand_id = vb.id " +
        "LEFT JOIN vehicle_type vt ON v.type_id = vt.id " +
        "LEFT JOIN identity_object io ON v.id = io.vehicle_id " +
        "LEFT JOIN persons pe ON pe.id = io.person_id WHERE((v.is_deleted = FALSE) " +
        "AND (LOWER(v.license_plate) LIKE %:keyword%) " +
        "AND (v.license_plate IN :licensePlates OR :licensePlates IS NULL) " +
        "AND (vt.id IN :vehicleTypeIds OR :vehicleTypeIds IS NULL))", nativeQuery = true)
  int total(
        @Param("keyword") String keyword,
        @Param("vehicleTypeIds") List<Long> vehicleTypeIds,
        @Param("licensePlates") List<String> licensePlates
  );


  @Transactional
  @Modifying
  @Query(value = "UPDATE vehicles SET is_deleted = true where id = ?1", nativeQuery = true)
  void deleteVehicle(long id);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.vehicle.VehicleDetail(v.id, v.licensePlate, vt.name, vb.name, p.name) " +
        "        FROM Vehicle v " +
        "        LEFT JOIN VehicleBrand vb ON v.brandId = vb.id " +
        "        LEFT JOIN VehicleType vt ON v.typeId = vt.id " +
        "        LEFT JOIN IdentityObject io ON v.id = io.id.vehicleId " +
        "        LEFT JOIN Person p ON io.id.personId = p.id " +
        "        WHERE v.id = :id")
  VehicleDetail detail(@Param("id") Long id);


  @Query(value = "select v.vehicle_card_id from vehicles v WHERE v.id = ?1", nativeQuery = true)
  Long findVehicleCardId(Long idVehicle);


  @Query("select new com.ncsgroup.ems.dto.response.vehicle.VehicleLicensePlateResponse(v.id, v.licensePlate, io.id.personId) " +
        "from Vehicle v " +
        "left join IdentityObject io on v.id = io.id.vehicleId " +
        "where :keyword is null " +
        "or v.licensePlate like %:keyword%")
  List<VehicleLicensePlateResponse> searchLicensePlate(String keyword, Pageable pageable);

  @Query("select count(v) " +
        "from Vehicle v " +
        "where :keyword is null " +
        "or v.licensePlate like %:keyword%")
  long countSearchLicensePlate(String keyword);

  @Query("select new com.ncsgroup.ems.dto.response.vehicle.VehicleLicensePlateResponse(v.id, v.licensePlate, io.id.personId) " +
        "from Vehicle v " +
        "left join IdentityObject io on v.id = io.id.vehicleId ")
  List<VehicleLicensePlateResponse> getAllLicensePlate();

  @Query(value = "SELECT v.license_plate FROM  vehicles v  WHERE v.id IN :vehicleIds", nativeQuery = true)
  List<String> findLicensePlates(List<Long> vehicleIds);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.identity.IdentityResponse(v.id, io.personId, io.groupId, v.licensePlate ) " +
        "FROM  Vehicle v " +
        "join IdentityObjectGroup io on v.id = io.vehicleId " +
        "WHERE io.groupId = :group_id ")
  List<IdentityResponse> getByGroupId(@Param("group_id") Long groupId);

//  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.identity.IdentityResponse(v.id, io.personId, io.groupId, v.licensePlate ) " +
//        "FROM Vehicle v " +
//        "JOIN IdentityObjectGroup io on v.id = io.vehicleId " +
//        "WHERE io.groupId IS NULL ")
//  List<IdentityResponse> getVehicleByGroupId();

  @Query(value = "SELECT v.id from Vehicle v join IdentityObject" +
        " io on v.id = io.id.vehicleId where io.id.personId = :personId order by v.id DESC ")
  List<Long> getByPersonId(long personId);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.identity.IdentityResponse(v.id, io.personId, io.groupId, v.licensePlate ) " +
        "FROM  Vehicle v " +
        "join IdentityObjectGroup io on v.id = io.vehicleId " +
        "WHERE io.groupId IN :group_id ")
  List<IdentityResponse> getByGroupIds(@Param("group_id") List<Long> groupId);


  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.identity.IdentityResponse(v.id, io.personId, io.groupId, v.licensePlate ) " +
        "FROM  Vehicle v " +
        "join IdentityObjectGroup io on v.id = io.vehicleId " +
        "WHERE io.groupId NOT IN :group_id ")
  List<IdentityResponse> getByOutsideGroupIds(@Param("group_id") List<Long> groupId);


  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.identity.IdentityResponse(v.id, io.id.personId, groups.id, v.licensePlate ) " +
        "FROM  Vehicle v " +
        "JOIN IdentityObject io ON v.id = io.id.vehicleId " +
        "JOIN IdentityObjectGroup groups ON io.id.personId = groups.personId " +
        "WHERE io.id.personId = :person_id ")
  List<IdentityResponse> getByPersonId(@Param("person_id") Long personId);

  @Query(value = "select v.id from Vehicle v join IdentityObjectGroup iog on iog.vehicleId = v.id" +
        " where (:groupId = 0 or iog.groupId = :groupId) and (:keyword is null or v.licensePlate like %:keyword%)")
  List<Long> getByGroupId(String keyword, long groupId);

}
