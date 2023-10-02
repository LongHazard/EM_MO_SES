package com.ncsgroup.ems.entity.vehicle;

import com.ncsgroup.ems.dto.request.address.AddressRequest;
import com.ncsgroup.ems.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicle_card")
public class VehicleCard extends BaseEntityWithUpdater {
  private Long cardTypeId;
  private String cardCode;
  private Long brandId;
  private Long vehicleTypeId;
  private Long registrationDate;
  private String licensePlate;
  private Long permanentResident;
}
