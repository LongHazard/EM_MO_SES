package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.address.province.ProvinceResponse;
import com.ncsgroup.ems.entity.address.Province;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProvinceRepository extends BaseRepository<Province> {
  @Query("SELECT new com.ncsgroup.ems.dto.response.address.province.ProvinceResponse" +
        " (p.code, p.name, p.nameEn, p.fullName, p.fullNameEn, p.codeName) " +
        "FROM Province p " +
        "WHERE :keyword is null or ( " +
        "lower( p.name) LIKE :keyword% OR " +
        "lower( p.nameEn) LIKE :keyword% OR " +
        "lower( p.fullName) LIKE :keyword% OR " +
        "lower( p.fullNameEn) LIKE :keyword% OR " +
        "lower( p.codeName) LIKE :keyword%) " +
        "ORDER BY p.name")
  List<ProvinceResponse> search(String keyword, Pageable pageable);

  @Query("SELECT count(p) " +
        "FROM Province p " +
        "WHERE :keyword is null or ( " +
        "lower( p.name) LIKE :keyword% OR " +
        "lower( p.nameEn) LIKE :keyword% OR " +
        "lower( p.fullName)  LIKE :keyword% OR " +
        "lower( p.fullNameEn) LIKE :keyword% OR " +
        "lower( p.codeName) LIKE :keyword%)")
  int countSearch(String keyword);
}
