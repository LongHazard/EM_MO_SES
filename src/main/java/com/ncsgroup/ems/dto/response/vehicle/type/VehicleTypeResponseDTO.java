package com.ncsgroup.ems.dto.response.vehicle.type;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VehicleTypeResponseDTO {
  private Long id;
  private String name;

  public VehicleTypeResponseDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
