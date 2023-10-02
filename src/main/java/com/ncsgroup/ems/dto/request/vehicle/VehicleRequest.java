package com.ncsgroup.ems.dto.request.vehicle;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.request.image.ImageRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.ncsgroup.ems.constanst.ExceptionConstants.ExceptionMessage.LICENSE_PLATE_NOT_EMPTY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VehicleRequest {
  private Long brandId;
  private Long typeId;
  @NotEmpty(message = LICENSE_PLATE_NOT_EMPTY)
  private String licensePlate;
  private List<ImageRequest> images = new ArrayList<>();
  private List<Long> colorIds = new ArrayList<>();

  public VehicleRequest(Long brandId, Long typeId, String licensePlate) {
    this.brandId = brandId;
    this.typeId = typeId;
    this.licensePlate = licensePlate;
  }
}
