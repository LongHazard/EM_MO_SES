package com.ncsgroup.ems.entity.person;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@Table(name = "card_type")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardType {
  @Id
  private long id;
  private String name;
  private String identity;
  private String description;

//  @PrePersist
//  public void ensureId() {
//    this.id = StringUtils.isBlank(this.id) ? UUID.randomUUID().toString() : this.id;
//  }
}
