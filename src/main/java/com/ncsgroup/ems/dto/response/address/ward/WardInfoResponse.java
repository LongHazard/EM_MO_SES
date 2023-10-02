package com.ncsgroup.ems.dto.response.address.ward;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class WardInfoResponse {
  private String wardName;
  private String wardNameEn;
  private String wardCodeName;

  public WardInfoResponse(String wardName, String wardNameEn, String wardCodeName) {
    this.wardName = wardName;
    this.wardNameEn = wardNameEn;
    this.wardCodeName = wardCodeName;
  }
}
