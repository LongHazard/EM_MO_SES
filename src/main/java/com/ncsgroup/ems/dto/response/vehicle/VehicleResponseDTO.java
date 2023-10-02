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
public class VehicleResponseDTO {
  private Long id;
  private Long brandId;
  private Long typeId;
  private String licensePlate;
  private Long vehicleCardId;
  private List<ImageResponse> images;
  private List<String> colorNames;


  public VehicleResponseDTO(Long id, Long brandId, Long typeId, String licensePlate, Long vehicleCardId) {
    this.id = id;
    this.brandId = brandId;
    this.typeId = typeId;
    this.licensePlate = licensePlate;
    this.vehicleCardId = vehicleCardId;
  }
}
