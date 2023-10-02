package com.ncsgroup.ems.entity.embed;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class VehicleCardColorId implements Serializable {
  private long vehicleCardId;
  private long colorId;
}
