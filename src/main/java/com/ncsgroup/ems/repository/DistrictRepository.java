package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.address.district.DistrictResponse;
import com.ncsgroup.ems.entity.address.District;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistrictRepository extends BaseRepository<District> {

  @Query("SELECT new com.ncsgroup.ems.dto.response.address.district.DistrictResponse" +
        " (d.code, d.name, d.nameEn, d.fullName, d.fullNameEn, d.codeName) " +
        "FROM District d " +
        "WHERE (:keyword is null or ( " +
        "lower( d.name) LIKE :keyword% OR " +
        "lower( d.nameEn) LIKE :keyword% OR " +
        "lower( d.fullName) LIKE :keyword% OR " +
        "lower( d.fullNameEn) LIKE :keyword% OR " +
        "lower( d.codeName) LIKE :keyword%)) and (:provinceCode is null  or d.provinceCode = :provinceCode) order by d.name")
  List<DistrictResponse> search(String keyword, String provinceCode, Pageable pageable);

  @Query("SELECT count(d)" +
        "FROM District d " +
        "WHERE (:keyword is null or ( " +
        "lower( d.name) LIKE :keyword% OR " +
        "lower( d.nameEn) LIKE :keyword% OR " +
        "lower( d.fullName) LIKE :keyword% OR " +
        "lower( d.fullNameEn) LIKE :keyword% OR " +
        "lower( d.codeName) LIKE :keyword%)) and (:provinceCode is null  or d.provinceCode = :provinceCode)")
  int countSearch(String keyword, String provinceCode);

}
