package com.ncsgroup.ems.dto.response.address;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.response.address.district.DistrictInfoResponse;
import com.ncsgroup.ems.dto.response.address.province.ProvinceInfoResponse;
import com.ncsgroup.ems.dto.response.address.ward.WardInfoResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressResponse {
  private WardInfoResponse wards;
  private DistrictInfoResponse districts;
  private ProvinceInfoResponse provinces;

  public AddressResponse(
        String wardName,
        String wardNameEn,
        String wardCodeName,
        String districtName,
        String districtNameEn,
        String districtCodeName,
        String provinceName,
        String provinceNameEn,
        String provinceCodeName
  ) {
    this.wards = new WardInfoResponse(wardName, wardNameEn, wardCodeName);
    this.districts = new DistrictInfoResponse(districtName, districtNameEn, districtCodeName);
    this.provinces = new ProvinceInfoResponse(provinceName, provinceNameEn, provinceCodeName);
  }
}
