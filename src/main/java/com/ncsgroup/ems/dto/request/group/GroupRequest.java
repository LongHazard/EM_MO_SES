package com.ncsgroup.ems.dto.request.group;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GroupRequest {
  private Long parentId;
  private String name;
  private String description;
  private List<Long> subIds;
  private List<Long> personIds;
  private List<Long> vehicleIds;
}
