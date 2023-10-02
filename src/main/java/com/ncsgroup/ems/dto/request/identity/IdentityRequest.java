package com.ncsgroup.ems.dto.request.identity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentityRequest {
  private Long id;
  private String type;
}
