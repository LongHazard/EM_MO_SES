package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.entity.identity.Image;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends BaseRepository<Image> {


  @Modifying
  @Query(value = "UPDATE Image as image SET " +
        "image.vehicleId = :vehicle_id, " +
        "image.type = :type, " +
        "image.personId = :person_id, " +
        "image.vehicleCardId = :vehicle_card_id, " +
        "image.personCardId = :person_card_id " +
        "WHERE image.id IN :image_ids "
  )
  void updateImageToObject(
        @Param("image_ids") List<Long> ids,
        @Param("vehicle_id") Long vehicleId,
        @Param("person_id") Long personId,
        @Param("vehicle_card_id") Long vehicleCardId,
        @Param("person_card_id") Long personCardId,
        @Param("type") String type
  );

  @Modifying
  @Query(value = "UPDATE Image as image SET " +
        "image.vehicleId = :vehicle_id, " +
        "image.type = :type, " +
        "image.personId = :person_id, " +
        "image.vehicleCardId = :vehicle_card_id, " +
        "image.personCardId = :person_card_id " +
        "WHERE image.id = :image_id "
  )
  void updateImageToObject(
        @Param("image_id") Long id,
        @Param("vehicle_id") Long vehicleId,
        @Param("person_id") Long personId,
        @Param("vehicle_card_id") Long vehicleCardId,
        @Param("person_card_id") Long personCardId,
        @Param("type") String type
  );
  @Query("select i from Image i where i.id in :imageIds order by i.id desc ")
  List<Image> findAllByIdIn(List<Long> imageIds);

  @Query(value = "SELECT * FROM images WHERE vehicle_id = :vehicle_id and is_deleted = false" +
        " order by id desc", nativeQuery = true)
  List<Image> findAllByVehicleId(@Param("vehicle_id") Long vehicleId);

  @Query(value = "SELECT * FROM images WHERE vehicle_id = :vehicle_id and is_deleted = false" +
        " order by id desc limit 1", nativeQuery = true)
  Optional<Image> findByVehicleId(@Param("vehicle_id") Long vehicleId);

  @Query(value = "SELECT * FROM images WHERE person_id = :person_id and is_deleted = false" +
        " order by id desc ", nativeQuery = true)
  List<Image> findAllByPersonId(@Param("person_id") Long personId);

  @Query(value = "SELECT * FROM images WHERE person_id = :person_id and is_deleted = false " +
        "order by id desc limit 1", nativeQuery = true)
  Optional<Image> findByPersonId(@Param("person_id") Long personId);

  @Query(value = "SELECT * FROM images WHERE vehicle_card_id = :vehicle_card_id and is_deleted = false" +
        " order by id desc", nativeQuery = true)
  List<Image> findAllByVehicleCardId(@Param("vehicle_card_id") Long vehicleCardId);

  @Query(value = "SELECT * FROM images WHERE vehicle_card_id = :vehicle_card_id and is_deleted = false " +
        "order by id desc limit 1", nativeQuery = true)
  Optional<Image> findByVehicleCardId(@Param("vehicle_card_id") Long vehicleCardId);

  @Query(value = "SELECT * FROM images WHERE person_card_id = :person_card_id and is_deleted = false" +
        " order by id desc", nativeQuery = true)
  List<Image> findAllByPersonCardId(@Param("person_card_id") Long personCardId);

  @Query(value = "SELECT * FROM images WHERE person_card_id = :person_card_id and is_deleted = false" +
        " order by id desc limit 1", nativeQuery = true)
  Optional<Image> findByPersonCardId(@Param("person_card_id") Long personCardId);

  @Query("select i.id from Image i where i.vehicleId = :vehicleId")
  List<Long> findIdByVehicleId(Long vehicleId);

  @Query("select i.id from Image i where i.personId = :personId")
  List<Long> findIdByPersonId(Long personId);

  @Query("select i.id from Image i where i.vehicleCardId = :vehicleCardId")
  List<Long> findIdByVehicleCardId(Long vehicleCardId);

  @Query("select i.id from Image i where i.personCardId = :personCardId")
  List<Long> findIdByPersonCardId(Long personCardId);

  @Modifying
  @Query(value = "UPDATE images set is_deleted = true where id= :id", nativeQuery = true)
  void softDelete(Long id);

  @Modifying
  @Query(value = "UPDATE Image i set i.isDeleted = true where i.id in :ids")
  void softDelete(List<Long> ids);

}
