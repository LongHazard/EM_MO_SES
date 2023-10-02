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
@Table(name = "person_card")
public class PersonCard extends BaseEntityWithUpdater {
  private Long cardTypeId;
  private String cardCode;
  private String dateOfBirth;
  private String sex;
  private String nationality;
//  private String placeOfOrigin;
//  private String permanentResident;
  private Long issueOn;
  private Long dateOfExpiry;
  private String placeOfIssue;
}
