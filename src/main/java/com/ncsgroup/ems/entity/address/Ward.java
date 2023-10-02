package com.ncsgroup.ems.entity.address;

import com.ncsgroup.ems.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "wards")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ward {
  @Id
  private String code;
  private String name;
  private String nameEn;
  private String fullName;
  private String fullNameEn;
  private String codeName;
  private String districtCode;
}
