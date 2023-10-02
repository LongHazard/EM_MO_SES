package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.group.GroupResponse;
import com.ncsgroup.ems.dto.response.group.IdentityGroupResponse;
import com.ncsgroup.ems.dto.response.group.VehicleGroupResponse;
import com.ncsgroup.ems.entity.identity.Group;
import com.ncsgroup.ems.entity.person.Person;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends BaseRepository<Group> {
  boolean existsByName(String name);

  @Query(value = "SELECT CASE WHEN COUNT(g) > 0 THEN TRUE ELSE FALSE END " +
        "FROM Group g WHERE g.id IN :groupIds AND g.parentId IS NOT NULL")
  boolean checkExistParentIds(List<Long> groupIds);


// xem lai
  @Query(value = "SELECT CASE WHEN COUNT(g) = COUNT(DISTINCT g.id) THEN true ELSE false END " +
        "FROM Group g WHERE g.id IN :groupIds")
  boolean findListIdExist(List<Long> groupIds);

  @Query(value = "SELECT g.\"name\" FROM  groups g  WHERE g.id IN :groupIds", nativeQuery = true)
  List<String> findNames(List<Long> groupIds);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.group.VehicleGroupResponse(g.id, g.name)  FROM  Group g  WHERE g.id IN :groupIds")
  List<VehicleGroupResponse> findVehicleGroupResponse(List<Long> groupIds);

  @Query("select new com.ncsgroup.ems.dto.response.group.GroupResponse" +
        "(g.id, g.parentId, g.name, g.description) " +
        "from Group g")
  List<GroupResponse> getSubGroupIdsById(Pageable pageable);

  @Query("select new com.ncsgroup.ems.dto.response.group.GroupResponse" +
        "(g.id, g.name, g.description) " +
        "from Group g where g.parentId = null")
  List<GroupResponse> getSubGroups();

  @Query(value = "SELECT g.name FROM  groups g  WHERE g.parent_id = :parentId", nativeQuery = true)
  List<String> findSubNames(Long parentId);

  @Query(value = "SELECT p.\"name\" FROM persons p " +
        "JOIN identity_object_group io ON p.id = io.person_id " +
        "JOIN groups g ON g.id = io.group_id " +
        "WHERE g.id = ?1", nativeQuery = true)
  List<String> findPersonNames(Long groupIds);

  @Query(value = "SELECT v.license_plate FROM vehicles v " +
        "JOIN identity_object_group io ON v.id = io.vehicle_id " +
        "JOIN groups g ON g.id = io.group_id " +
        "WHERE g.id = ?1", nativeQuery = true)
  List<String> findLicensePlates(Long groupIds);

  @Query(value = "select new com.ncsgroup.ems.dto.response.group.IdentityGroupResponse( " +
        " g.id, g.name) from Group g where g.id in :ids")
  List<IdentityGroupResponse> getById(List<Long> ids);

  @Query("select new com.ncsgroup.ems.dto.response.group.VehicleGroupResponse(g.id, g.name) " +
        "from Vehicle v " +
        "left join IdentityObjectGroup iog on v.id = iog.vehicleId " +
        "left join Group g on iog.groupId = g.id " +
        "where v.id = :vehicleId")
  VehicleGroupResponse get(Long vehicleId);

  @Query(value = "WITH RECURSIVE group_rec  (id, parent_id, name, description, processed) AS ( " +
        "  SELECT id, parent_id, name, description, ARRAY[id] " +
        "  FROM groups " +
        "  WHERE id = :group_id " +
        "  UNION ALL " +
        "  SELECT t.id, t.parent_id, t.name, t.description, processed || t.id\n" +
        "  FROM groups t" +
        "  INNER JOIN group_rec r ON t.parent_id = r.id\n" +
        "  WHERE NOT t.id = ANY(r.processed)\n" +
        ") " +
        "SELECT id " +
        "FROM group_rec", nativeQuery = true)
  List<Long> getSubGroupIdsById(@Param("group_id") Long groupId);

  @Query(value = "WITH RECURSIVE cte (id, parent_id, name) AS (\n" +
        "  SELECT id, parent_id, name\n" +
        "  FROM groups " +
        "  WHERE id = 4 " +
        "  UNION ALL " +
        "  SELECT t.id, t.parent_id, t.name " +
        "  FROM groups t " +
        "  INNER JOIN cte ON  cte.parent_id = t.id ) " +
        "SELECT id, parent_id, name " +
        "FROM cte " +
        "WHERE parent_id is null",
        nativeQuery = true
  )
  Long getRootGroupId(Long groupId);
  @Query("select new com.ncsgroup.ems.dto.response.group.VehicleGroupResponse(g.id, g.name) " +
        "from Group g " +
        "left join IdentityObjectGroup iog on g.id = iog.groupId " +
        "left join Vehicle v on iog.vehicleId = v.id " +
        "where v.id = :vehicleId")
  List<VehicleGroupResponse> find(Long vehicleId);

  @Transactional
  @Modifying
  @Query(value = "UPDATE groups SET is_deleted = true where id = ?1", nativeQuery = true)
  void remove(Long id);

  @Query(value = "select iog.groupId from IdentityObjectGroup iog where iog.personId = :personId")
  List<Long> getByPersonId(Long personId);
}
