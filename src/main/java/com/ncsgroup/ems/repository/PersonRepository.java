package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import com.ncsgroup.ems.dto.response.person.PersonCreateResponse;
import com.ncsgroup.ems.dto.response.person.PersonDTO;
import com.ncsgroup.ems.dto.response.person.PersonResponse;
import com.ncsgroup.ems.entity.person.Person;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PersonRepository extends BaseRepository<Person> {
  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);

  boolean existsByFaceId(String faceId);

  boolean existsByStaffCode(String staffCode);

  boolean existsByPersonCardId(String id);

  boolean existsByUid(String uid);

  @Query("SELECT new com.ncsgroup.ems.dto.response.person.PersonResponse(" +
        " p.id, p.uid, p.name, p.email, p.sex, p.phoneNumber, p.staffCode, p.position, p.department, p.faceId) " +
        "FROM Person p WHERE " +
        " (:keyword is null or lower(p.name) LIKE %:keyword%) AND (p.faceId IN :faceIds ) AND p.isDeleted = false" +
        " order by p.id DESC ")
  List<PersonResponse> listBySearch(String keyword, List<String> faceIds, Pageable pageable);

  @Query("SELECT count(p) " +
        "FROM Person p WHERE p.isDeleted = false AND " +
        " ( :keyword is null or lower(p.name) LIKE %:keyword%) AND (p.faceId IN :faceIds) ")
  int countSearch(@Param("keyword") String keyword, @Param("faceIds") List<String> faceIds);

  @Query("SELECT new com.ncsgroup.ems.dto.response.person.PersonResponse(" +
        " p.id,p.uid, p.name, p.email, p.sex, p.phoneNumber, p.staffCode, p.position, p.department, p.faceId) " +
        "FROM Person p WHERE " +
        " (:keyword is null or lower(p.name) LIKE %:keyword%) AND p.isDeleted = false order by p.id desc ")
  List<PersonResponse> listByKeyword(String keyword, Pageable pageable);

  @Query("SELECT count(p) " +
        "FROM Person p WHERE p.isDeleted = false AND " +
        " ( :keyword is null or lower(p.name) LIKE %:keyword%)")
  int countByKeyword(@Param("keyword") String keyword);

  @Query("select new com.ncsgroup.ems.dto.response.person.PersonResponse(" +
        " p.id, p.uid, p.name, p.email, p.sex, p.phoneNumber, p.staffCode, p.position, p.department, p.faceId " +
        " ) from Person p join IdentityObjectGroup iog on p.id = iog.personId and iog.groupId = :groupId " +
        " order by p.id DESC ")
  List<PersonResponse> listByGroup(long groupId, Pageable pageable);

  @Query("select count (p)" +
        " from Person p join IdentityObjectGroup iog on p.id = iog.personId and iog.groupId = :groupId ")
  int countByGroup(long groupId);

  @Query(value = "select p.personCardId from Person p where p.id =:personId")
  Long getPersonCarId(Long personId);

  @Query(value = "SELECT COUNT(io) FROM identity_object io WHERE io.person_id = :personId", nativeQuery = true)
  int countVehicle(Long personId);

  @Query(value = "select new com.ncsgroup.ems.dto.response.person.PersonCreateResponse(" +
        "p.id, p.name, p.uid,p.sex,p.email, p.phoneNumber, p.staffCode, p.position, p.department, p.faceId) from Person  p" +
        " where p.id=:id")
  PersonCreateResponse detail(Long id);

  @Query(value = "select p.id from Person  p" +
        " join IdentityObject io on io.id.personId = p.id  where io.id.vehicleId=:id")
  Long getIdByVehicle(Long id);

  @Query(value = "SELECT p.\"name\" FROM  persons p  WHERE p.id IN :personIds", nativeQuery = true)
  List<String> findNames(List<Long> personIds);

  @Query(value = "SELECT p.id FROM  persons p  WHERE p.uid = ?1", nativeQuery = true)
  Long getIdByUid(String uid);

  @Query(value = "SELECT p.\"name\" FROM  persons p  WHERE p.id = ?1", nativeQuery = true)
  String findNameById(Long personId);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.identity.IdentityResponse(io.vehicleId, p.id, io.groupId, p.name ) " +
        "FROM  Person p " +
        "join IdentityObjectGroup io on p.id = io.personId " +
        "WHERE io.groupId = :group_id ")
  List<IdentityResponse> getPersonByGroupId(@Param("group_id") Long groupId);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.identity.IdentityResponse(io.vehicleId, p.id, io.groupId, p.name ) " +
        "FROM  Person p " +
        "join IdentityObjectGroup io on p.id = io.personId " +
        "WHERE io.groupId IN :group_id ")
  List<IdentityResponse> getPersonByGroupId(@Param("group_id") List<Long> groupIds);

  @Query(value = " select s.department from persons s where :keyword is null or LOWER(s.department) like %:keyword%", nativeQuery = true)
  Set<String> findDepartment(String keyword);

  @Query(value = " select s.\"position\" from persons s where :keyword is null or LOWER(s.\"position\") like %:keyword%", nativeQuery = true)
  Set<String> findPosition(String keyword);

  @Query(value = "select s.org from persons s where :keyword is null or LOWER(s.org) like %:keyword%", nativeQuery = true)
  Set<String> findOrg(String keyword);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.identity.IdentityResponse(io.vehicleId, p.id, io.groupId, p.name) " +
        "FROM  Person p " +
        "join IdentityObjectGroup io on p.id = io.personId " +
        "WHERE io.groupId NOT IN :group_id ")
  List<IdentityResponse> getByOutsideGroupIds(@Param("group_id") List<Long> groupId);

  @Query(value = "UPDATE Person p SET p.personCardId = :personCardId where p.id = :id")
  @Modifying
  @Transactional
  void updatePersonCardId(long id, long personCardId);

  @Query("select p from Person p where p.uid = :uid")
  Optional<Person> findBy(String uid);

  @Transactional
  @Modifying
  @Query(value = "UPDATE persons SET is_deleted = true where id = ?1", nativeQuery = true)
  void remove(long id);

  @Query("select p from Person p " +
        "join IdentityObject io on p.id = io.id.personId " +
        "join Vehicle v on io.id.vehicleId = v.id " +
        "where v.id = :vehicleId")
  Optional<Person> findByVehicleId(long vehicleId);

  @Query("select new com.ncsgroup.ems.dto.response.person.PersonDTO(" +
        " p.id, p.uid, p.name ) from Person p where :keyword is null  or lower(p.uid) like :keyword%" +
        " or lower(p.name) like :keyword% order by p.name")
  List<PersonDTO> list(String keyword, Pageable pageable);

  @Query("select new com.ncsgroup.ems.dto.response.person.PersonDTO(" +
        " p.id, p.uid, p.name ) from Person p where p.id IN :personIds" )
  List<PersonDTO> list(List<Long> personIds);

  @Query("select count (p) from Person p where :keyword is null  or lower(p.uid) like :keyword%" +
        " or lower(p.name) like :keyword% ")
  int countSearch(String keyword);
}
