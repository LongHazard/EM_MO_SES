package com.ncsgroup.ems.dto.request.person;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonSearchRequest {
  private String keyword;
}
