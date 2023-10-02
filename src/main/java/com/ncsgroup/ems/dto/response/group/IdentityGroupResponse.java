package com.ncsgroup.ems.dto.response.group;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class IdentityGroupResponse {
  private long id;
  private String name;

  public IdentityGroupResponse(long id, String name) {
    this.id = id;
    this.name = name;
  }
}
