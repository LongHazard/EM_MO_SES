package com.ncsgroup.ems.entity.embed;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Embeddable
public class VehicleColorId implements Serializable {
  private Long vehicleId;
  private Long colorId;
}
