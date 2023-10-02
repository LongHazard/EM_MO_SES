package com.ncsgroup.ems.dto.response.person;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class PersonDTO {
  private Long id;
  private String uid;
  private String name;

  public PersonDTO(Long id, String uid, String name) {
    this.id = id;
    this.uid = uid;
    this.name = name;
  }
}
