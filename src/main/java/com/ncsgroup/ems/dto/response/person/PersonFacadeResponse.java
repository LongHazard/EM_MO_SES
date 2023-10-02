package com.ncsgroup.ems.dto.response.person;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.response.group.IdentityGroupResponse;
import com.ncsgroup.ems.dto.response.personcard.PersonCardResponse;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonFacadeResponse {
  private PersonCreateResponse person;
  private PersonCardResponse personCard;
  private List<IdentityGroupResponse> groups;

  public PersonFacadeResponse(PersonCreateResponse person, PersonCardResponse personCard) {
    this.person = person;
    this.personCard = personCard;
  }
}
