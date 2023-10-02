package com.ncsgroup.ems.dto.response.vehicle;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VehicleLicensePlatePageResponse {
  private List<VehicleLicensePlateResponse> licensePlates;
  private long numberOfLicensePlate;
}
