package com.ncsgroup.ems.entity.identity;

import com.ncsgroup.ems.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image extends BaseEntityWithUpdater {
  private String url;
  private String contentType;
  private String name;
  private String bucketName;
  private String type;
  private Long vehicleId;
  private Long personId;
  private Long vehicleCardId;
  private Long personCardId;
  private boolean isDeleted;
}
