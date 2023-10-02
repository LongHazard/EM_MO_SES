package com.ncsgroup.ems.dto.response.address.district;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DistrictInfoResponse {
  private String districtName;
  private String districtNameEn;
  private String districtCodeName;

  public DistrictInfoResponse(String districtName, String districtNameEn, String districtCodeName) {
    this.districtName = districtName;
    this.districtNameEn = districtNameEn;
    this.districtCodeName = districtCodeName;
  }
}
