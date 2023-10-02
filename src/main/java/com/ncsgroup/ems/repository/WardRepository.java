package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.address.ward.WardResponse;
import com.ncsgroup.ems.entity.address.Ward;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WardRepository extends BaseRepository<Ward> {

  @Query("SELECT new com.ncsgroup.ems.dto.response.address.ward.WardResponse" +
        " (w.code, w.name, w.nameEn, w.fullName, w.fullNameEn, w.codeName) " +
        "FROM Ward w " +
        "WHERE (:keyword is null or ( " +
        "lower( w.name) LIKE :keyword% OR " +
        "lower( w.nameEn) LIKE :keyword% OR " +
        "lower( w.fullName) LIKE :keyword% OR " +
        "lower( w.fullNameEn) LIKE :keyword% OR " +
        "lower( w.codeName) LIKE :keyword%)) and (:districtCode is null  or w.districtCode = :districtCode) ORDER by w.name")
  List<WardResponse> search(String keyword, String districtCode, Pageable pageable);

  @Query("SELECT count(w)" +
        "FROM Ward w " +
        "WHERE (:keyword is null or ( " +
        "lower( w.name) LIKE :keyword% OR " +
        "lower( w.nameEn) LIKE :keyword% OR " +
        "lower( w.fullName) LIKE :keyword% OR " +
        "lower( w.fullNameEn) LIKE :keyword% OR " +
        "lower( w.codeName) LIKE :keyword%)) and (:districtCode is null  or w.districtCode = :districtCode)")
  int countSearch(String keyword, String districtCode);
}
