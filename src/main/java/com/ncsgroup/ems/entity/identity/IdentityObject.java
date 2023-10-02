package com.ncsgroup.ems.entity.identity;

import com.ncsgroup.ems.entity.embed.IdentityObjectId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "identity_object")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityObject {
  @EmbeddedId
  private IdentityObjectId id;

  private Integer type;

  public static IdentityObject from(Long vehicleId, Long personId){
    return IdentityObject.builder().id(IdentityObjectId.of(vehicleId,personId)).build();
  }

  public IdentityObject(Long vehicleId, Long personId, Integer type) {
    this.id = IdentityObjectId.of(vehicleId, personId);
    this.type = type;
  }
}
