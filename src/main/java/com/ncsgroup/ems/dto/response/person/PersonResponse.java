package com.ncsgroup.ems.dto.response.person;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonResponse {
  private Long id;
  private String uid;
  private String name;
  private String sex;
  private String email;
  private String phoneNumber;
  private String staffCode;
  private String position;
  private String department;
  private String faceId;
  private int countVehicle;
  private ImageResponse images;
  public PersonResponse(
        Long id,
        String uid,
        String name,
        String email,
        String sex,
        String phoneNumber,
        String staffCode,
        String position,
        String department,
        String faceId
  ) {
    this.id = id;
    this.uid = uid;
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
