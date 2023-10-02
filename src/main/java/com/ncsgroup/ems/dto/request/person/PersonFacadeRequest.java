package com.ncsgroup.ems.dto.request.person;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.request.person.card.PersonCardRequest;
import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonFacadeRequest {
  @Valid
  private PersonRequest person;

  @Valid
  private PersonCardRequest personCard;

  private List<Long> groupIds;
}
