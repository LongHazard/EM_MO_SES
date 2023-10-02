package com.ncsgroup.ems.dto.request.address;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressRequest {
  private String provinceCode;
  private String districtCode;
  private String wardCode;
  private String addressDetail;
  private Long personCardId;
  private String type;

  public AddressRequest(String provinceCode, String districtCode, String wardCode, String addressDetail) {
    this.provinceCode = provinceCode;
    this.districtCode = districtCode;
    this.wardCode = wardCode;
    this.addressDetail = addressDetail;
  }
}
