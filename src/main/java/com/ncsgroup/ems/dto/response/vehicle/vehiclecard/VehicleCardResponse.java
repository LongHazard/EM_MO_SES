package com.ncsgroup.ems.dto.response.vehicle.vehiclecard;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.response.address.AddressResponse;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse;
import com.ncsgroup.ems.dto.response.color.ColorResponse;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import com.ncsgroup.ems.dto.response.vehicle.brand.VehicleBrandResponse;
import com.ncsgroup.ems.dto.response.vehicle.type.VehicleTypeResponseDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

import static com.ncsgroup.ems.utils.DateUtils.convertTimestampToDate;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class VehicleCardResponse {
  private Long id;
  private CardTypeResponse cardType;
  private String cardCode;
  private VehicleBrandResponse vehicleBrand;
  private VehicleTypeResponseDTO vehicleType;
  private String registrationDate;
  private String licensePlate;
  private AddressResponse permanentResident;
  private List<ImageResponse> images;
  private List<ColorResponse> colors;

  public void setRegistrationDate(Long registrationDate) {
    if (Objects.nonNull(registrationDate)) {
      this.registrationDate = convertTimestampToDate(registrationDate);
    }
  }
}
