package com.ncsgroup.ems.dto.response.vehicle;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeDetail;
import com.ncsgroup.ems.dto.response.group.VehicleGroupResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VehicleDetailFacadeResponse {
  private VehicleDetail vehicle;
  private CardTypeDetail vehicleCard;
  private VehicleGroupResponse group;

  public VehicleDetailFacadeResponse(Long id, String licensePlate, String nameVehicleType,
                                     String nameVehicleBrand, String owner,
                                     String cardType, String cardCode
  ) {
    this.vehicle = new VehicleDetail(id, licensePlate, nameVehicleType, nameVehicleBrand, owner);
    this.vehicleCard = new CardTypeDetail(cardType, cardCode);
  }
}
