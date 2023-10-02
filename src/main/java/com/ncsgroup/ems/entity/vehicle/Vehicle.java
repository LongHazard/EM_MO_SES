package com.ncsgroup.ems.entity.vehicle;

import com.ncsgroup.ems.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "vehicles")
public class Vehicle extends BaseEntityWithUpdater {
  private Long brandId;
  private Long typeId;
  private String licensePlate;
  private Long vehicleCardId;
  private boolean isDeleted;
  private String uid;

  public static Vehicle from(
        Long brandId,
        Long typeId,
        String licensePlate,
        Long vehicleCardId,
        String uid
  ) {
    return Vehicle
          .builder()
          .brandId(brandId)
          .typeId(typeId)
          .licensePlate(licensePlate)
          .vehicleCardId(vehicleCardId)
          .uid(uid)
          .build();
  }
}
