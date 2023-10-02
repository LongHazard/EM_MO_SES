package com.ncsgroup.ems.dto.response.identity;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

import static com.ncsgroup.ems.constanst.EMSConstants.GroupType.VEHICLE;
import static com.ncsgroup.ems.constanst.EMSConstants.ImageType.PERSON;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IdentityResponse {
  private Long vehicleId;
  private Long personId;
  private Long groupId;
  private String name;
  private String type;
  private List<IdentityResponse> vehicles;

  public IdentityResponse(
        Long vehicleId,
        Long personId,
        Long groupId,
        String name
  ) {
    if (Objects.nonNull(vehicleId)) {
      this.type = VEHICLE;
    } else {
      this.type = PERSON;
    }
    this.vehicleId = vehicleId;
    this.personId = personId;
    this.groupId = groupId;
    this.name = name;
  }
}
