package com.ncsgroup.ems.entity.vehicle;

import com.ncsgroup.ems.entity.embed.VehicleCardColorId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicle_card_color")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  VehicleCardColor {
  @EmbeddedId
  private VehicleCardColorId id;

  public static VehicleCardColor of(Long vehicleCardId, Long colorId) {
    return new VehicleCardColor(VehicleCardColorId.of(vehicleCardId, colorId));
  }
}
