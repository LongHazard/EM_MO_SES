package com.ncsgroup.ems.dto.response.person;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonCreateResponse {
  private Long id;
  private String name;
  private String uid;
  private String sex;
  private String email;
  private String phoneNumber;
  private String staffCode;
  private String position;
  private String department;
  private String faceId;
  private List<ImageResponse> images;

  public PersonCreateResponse(Long id, String name, String uid, String sex, String email, String phoneNumber,
                              String staffCode, String position, String department, String faceId) {
    this.id = id;
    this.name = name;
    this.uid = uid;
    this.sex = sex;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.staffCode = staffCode;
    this.position = position;
    this.department = department;
    this.faceId = faceId;
  }


}
