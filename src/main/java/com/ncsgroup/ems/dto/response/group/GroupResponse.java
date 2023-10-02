package com.ncsgroup.ems.dto.response.group;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.response.identity.IdentityResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class GroupResponse {
  private Long id;
  private Long parentId;
  private String name;
  private String description;
  private List<String> subNames;
  private List<String> personNames;
  private List<String> vehicleLicencePlate;
  private List<IdentityResponse> identities;

  public GroupResponse(
        Long id,
        Long parentId,
        String name,
        String description
  ) {
    this.id = id;
    this.parentId = parentId;
    this.name = name;
    this.description = description;
  }

  public GroupResponse(
        Long id,
        String name,
        String description
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
}
