package com.ncsgroup.ems.entity.address;

import com.ncsgroup.ems.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@Table(name = "address")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String provinceCode;
  private String districtCode;
  private String wardCode;
  private String addressDetail;
  private Long personCardId;
  private String type;
  private boolean isDeleted;

//  @PrePersist
//  public void ensureId() {
//    this.id = StringUtils.isBlank(this.id) ? UUID.randomUUID().toString() : this.id;
//  }
}
