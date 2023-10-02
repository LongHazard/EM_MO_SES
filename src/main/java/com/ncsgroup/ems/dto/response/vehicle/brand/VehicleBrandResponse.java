package com.ncsgroup.ems.dto.response.vehicle.brand;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VehicleBrandResponse {
  private Long id;
  private String name;

  public VehicleBrandResponse(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
