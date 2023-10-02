package com.ncsgroup.ems.entity.person;

import com.ncsgroup.ems.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "persons")
public class Person extends BaseEntityWithUpdater {
  private String name;
  private String uid;
  private String email;
  private String sex;
  private String phoneNumber;
  private String staffCode;
  private String position;
  private String department;
  private String faceId;
  private Long personCardId;
  private boolean isDeleted;
}
