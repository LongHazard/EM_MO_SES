package com.ncsgroup.ems.entity.base;


import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.ANONYMOUS;
import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.TIME_NOW;


@Data
@MappedSuperclass
public abstract class BaseEntityWithUpdater extends BaseEntity {
  @LastModifiedBy
  private Long lastUpdatedBy = ANONYMOUS;
  @LastModifiedDate
  private Long lastUpdatedAt = TIME_NOW;

}
