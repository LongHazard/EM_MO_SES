package com.ncsgroup.ems.dto.request.person;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.request.image.ImageRequest;
import com.ncsgroup.ems.validation.ValidationEmail;
import com.ncsgroup.ems.validation.ValidationPhoneNumber;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonRequest {
  private String name;
  @ValidationEmail
  private String email;
  private String sex;
  @ValidationPhoneNumber
  private String phoneNumber;
  private String staffCode;
  private String position;
  private String department;
  private String faceId;
  private List<ImageRequest> images;

  public PersonRequest(String name, String email, String sex, String phoneNumber, String staffCode, String position, String department, String faceId) {
    this.name = name;
    this.email = email;
    this.sex = sex;
    this.phoneNumber = phoneNumber;
    this.staffCode = staffCode;
    this.position = position;
    this.department = department;
    this.faceId = faceId;
  }
}
