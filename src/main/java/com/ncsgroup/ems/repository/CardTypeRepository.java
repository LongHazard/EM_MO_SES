package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.cardtype.CardTypeDetail;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse;
import com.ncsgroup.ems.entity.person.CardType;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardTypeRepository extends BaseRepository<CardType> {
  boolean existsByName(String name);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse(ct.id, ct.identity, ct.name, ct.description) " +
        "FROM CardType ct where :keyword is null " +
        "or lower(ct.name) like %:keyword%")
  List<CardTypeResponse> search(String keyword);

  @Query(value = "SELECT new com.ncsgroup.ems.dto.response.cardtype.CardTypeDetail(c.name, vc.cardCode)" +
        "FROM Vehicle v " +
        "LEFT JOIN VehicleCard vc on v.vehicleCardId = vc.id " +
        "LEFT JOIN CardType c on vc.cardTypeId = c.id WHERE v.id = :idVehicle")
  CardTypeDetail findCardType(Long idVehicle);
}
