package com.ncsgroup.ems.dto.response.color;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class ColorResponse {
  private Long id;
  private String name;

  public ColorResponse(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
