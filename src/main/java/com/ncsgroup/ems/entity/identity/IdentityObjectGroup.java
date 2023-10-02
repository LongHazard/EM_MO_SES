package com.ncsgroup.ems.entity.identity;

import com.ncsgroup.ems.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "identity_object_group")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityObjectGroup extends BaseEntityWithUpdater {
  private Long groupId;
  private Long vehicleId;
  private Long personId;

  public static IdentityObjectGroup of(Long groupId, Long vehicleId){
    return IdentityObjectGroup.builder()
          .groupId(groupId)
          .vehicleId(vehicleId)
          .build();
  }

}
