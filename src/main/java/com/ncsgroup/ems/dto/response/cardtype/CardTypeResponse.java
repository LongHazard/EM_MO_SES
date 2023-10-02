package com.ncsgroup.ems.dto.response.cardtype;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class CardTypeResponse {
  private Long id;
  private String identity;
  private String name;
  private String description;

  public CardTypeResponse(Long id, String identity, String name, String description) {
    this.id = id;
    this.identity = identity;
    this.name = name;
    this.description = description;
  }
}
