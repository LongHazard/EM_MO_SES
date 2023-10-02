package com.ncsgroup.ems.dto.response.personcard;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.response.address.AddressResponse;
import com.ncsgroup.ems.dto.response.cardtype.CardTypeResponse;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import com.ncsgroup.ems.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonCardResponse {
  private Long id;
  private CardTypeResponse cardType;
  private String cardCode;
  private String dateOfBirth;
  private String sex;
  private String nationality;
  private AddressResponse placeOfOrigin;
  private AddressResponse permanentResident;
  private Long issueOn;
  private String dateOfExpiry;
  private String placeOfIssue;
  private List<ImageResponse> images;

  public PersonCardResponse(Long id, Long cardTypeId, String identity, String name, String description,
                            String cardCode, String dateOfBirth, String sex, String nationality, Long issueOn,
                            Long dateOfExpiry, String placeOfIssue) {
    this.id = id;
    this.cardType = new CardTypeResponse(cardTypeId, identity, name, description);
    this.cardCode = cardCode;
    this.dateOfBirth = dateOfBirth;
    this.sex = sex;
    this.nationality = nationality;
    this.issueOn = issueOn;
    this.dateOfExpiry = Objects.isNull(dateOfExpiry) ? null : DateUtils.convertTimestampToDate(dateOfExpiry);
    this.placeOfIssue = placeOfIssue;
  }
}
