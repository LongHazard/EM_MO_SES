package com.ncsgroup.ems.dto.response.vehicle.vehiclecard;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VehicleCardPageResponse {
  private List<VehicleCardResponse> vehicleCardResponses;
  private Long count;
}
