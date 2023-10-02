package com.ncsgroup.ems.entity.base;


import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.ANONYMOUS;
import static com.ncsgroup.ems.constanst.EMSConstants.CommonConstants.TIME_NOW;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedBy
  private Long createdBy = ANONYMOUS;

  @CreatedDate
  private Long createdAt = TIME_NOW;

//  @PrePersist
//  public void ensureId() {
//    this.id = StringUtils.isBlank(this.id) ? UUID.randomUUID().toString() : this.id;
//  }

}

