package com.ncsgroup.ems.dto.response.vehicle;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VehicleDetail {
  private Long id;
  private String licensePlate;
  private String nameVehicleType;
  private String nameVehicleBrand;
  private String owner;
  private List<String> nameColor;
  private List<ImageResponse> images;

  public VehicleDetail(
        Long id,
        String licensePlate,
        String nameVehicleType,
        String nameVehicleBrand,
        String owner
  ) {
    this.id = id;
    this.licensePlate = licensePlate;
    this.nameVehicleType = nameVehicleType;
    this.nameVehicleBrand = nameVehicleBrand;
    this.owner = owner;
  }

  public VehicleDetail(Long id,
                       String licensePlate,
                       String nameVehicleType,
                       String nameVehicleBrand,
                       String owner,
                       List<String> nameColor,
                       List<ImageResponse> images) {
    this.id = id;
    this.licensePlate = licensePlate;
    this.nameVehicleType = nameVehicleType;
    this.nameVehicleBrand = nameVehicleBrand;
    this.owner = owner;
    this.nameColor = nameColor;
    this.images = images;
  }

  public VehicleDetail(
        Long id,
        String licensePlate,
        String nameVehicleType,
        String nameVehicleBrand
  ) {
    this.id = id;
    this.licensePlate = licensePlate;
    this.nameVehicleType = nameVehicleType;
    this.nameVehicleBrand = nameVehicleBrand;
  }

  public VehicleDetail(Long id, String licensePlate) {
    this.id = id;
    this.licensePlate = licensePlate;
  }
}
