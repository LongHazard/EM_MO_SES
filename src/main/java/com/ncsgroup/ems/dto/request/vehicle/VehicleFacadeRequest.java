package com.ncsgroup.ems.dto.request.vehicle;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.request.vehicle.card.VehicleCardRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VehicleFacadeRequest {
  private String personUid;

  private VehicleRequest vehicle;
  @Valid
  private VehicleCardRequest vehicleCard;

  private List<Long> groupIds;
}
