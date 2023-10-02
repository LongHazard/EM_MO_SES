package com.ncsgroup.ems.entity.vehicle;

import com.ncsgroup.ems.entity.embed.VehicleColorId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle_color")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleColor {
  @EmbeddedId
  private VehicleColorId id;

  public static VehicleColor of(Long vehicleId, Long colorId) {
    return VehicleColor.builder().id(VehicleColorId.of(vehicleId, colorId)).build();
  }

}
