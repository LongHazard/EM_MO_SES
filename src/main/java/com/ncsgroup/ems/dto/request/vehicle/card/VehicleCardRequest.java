package com.ncsgroup.ems.dto.request.vehicle.card;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.request.address.AddressOfVehicleRequest;
import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.validation.ValidDateFormat;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.ncsgroup.ems.constanst.ExceptionConstants.ExceptionMessage.LICENSE_PLATE_NOT_EMPTY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VehicleCardRequest {
  private Long id;
  private Long cardTypeId;
  private String cardCode;
  private Long brandId;
  private Long vehicleTypeId;
  @ValidDateFormat
  private String registrationDate;
  private String licensePlate;
  private AddressOfVehicleRequest permanentResident;
  private List<ImageRequest> images = new ArrayList<>();
  private List<Long> colorIds = new ArrayList<>();
}
