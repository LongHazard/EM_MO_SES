package com.ncsgroup.ems.entity.vehicle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@Entity
@Table(name = "vehicle_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String identity;
  private String name;
  private String description;
}
