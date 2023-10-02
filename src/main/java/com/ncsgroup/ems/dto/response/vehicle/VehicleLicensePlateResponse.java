package com.ncsgroup.ems.dto.response.vehicle;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VehicleLicensePlateResponse {
  private Long id;
  private String licensePlate;
  private Long personId;

  public VehicleLicensePlateResponse(Long id, String licensePlate, Long personId) {
    this.id = id;
    this.licensePlate = licensePlate;
    this.personId = personId;
  }

  public static VehicleLicensePlateResponse of(Long id, String licensePlate) {
    var vehicleLicensePlateResponse = new VehicleLicensePlateResponse();
    vehicleLicensePlateResponse.setId(id);
    vehicleLicensePlateResponse.setLicensePlate(licensePlate);
    return vehicleLicensePlateResponse;
  }
}
