package com.ncsgroup.ems.dto.response.group;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VehicleGroupResponse {
  private Long id;
  private String name;

  public VehicleGroupResponse(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public static VehicleGroupResponse of(Long id, String name) {
    VehicleGroupResponse vehicleGroupResponse = new VehicleGroupResponse();
    vehicleGroupResponse.setId(id);
    vehicleGroupResponse.setName(name);
    return vehicleGroupResponse;
  }
}
