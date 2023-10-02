package com.ncsgroup.ems.repository;

import com.ncsgroup.ems.dto.response.address.AddressResponse;
import com.ncsgroup.ems.entity.address.Address;
import com.ncsgroup.ems.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends BaseRepository<Address> {

  @Query(value = "select new com.ncsgroup.ems.dto.response.address.AddressResponse( " +
        "w.name, w.nameEn, w.codeName, d.name, d.nameEn, d.codeName, p.name, p.nameEn, p.codeName ) from " +
        "Address  a join Province  p join District d join Ward w on a.provinceCode = p.code" +
        " and a.districtCode = d.code and a.wardCode = w.code where a.id = :id")
  AddressResponse get(Long id);


  @Query(value = "select new com.ncsgroup.ems.dto.response.address.AddressResponse( " +
        "w.name, w.nameEn, w.codeName, d.name, d.nameEn, d.codeName, p.name, p.nameEn, p.codeName ) from " +
        "Address  a join Province  p join District d join Ward w on a.provinceCode = p.code" +
        " and a.districtCode = d.code and a.wardCode = w.code where a.personCardId = :personCardId and a.type = :type")
  AddressResponse getAddressOfPersonCard(long personCardId, String type);

  @Query(value = "select a.id from Address a where a.personCardId = :personCardId and a.type = :type")
  Long getId(Long personCardId, String type);


}
