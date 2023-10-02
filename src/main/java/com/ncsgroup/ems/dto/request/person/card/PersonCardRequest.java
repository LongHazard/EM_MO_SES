package com.ncsgroup.ems.dto.request.person.card;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.request.address.AddressRequest;
import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.validation.ValidationDateFormat;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonCardRequest {
  private Long cardTypeId;
  private String cardCode;
  @ValidationDateFormat
  private String dateOfBirth;
  private String sex;
  private String nationality;
  private AddressRequest placeOfOrigin;
  private AddressRequest permanentResident;
  private Long issueOn;
  @ValidationDateFormat
  private String dateOfExpiry;
  private String placeOfIssue;
  private List<ImageRequest> images;
}
