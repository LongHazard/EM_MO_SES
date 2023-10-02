package com.ncsgroup.ems.dto.response.cardtype;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.ems.dto.response.image.ImageResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CardTypeDetail {
  private String cardType;
  private String cardCode;
  private List<ImageResponse> images;

  public CardTypeDetail(String cardType, String cardCode) {
    this.cardType = cardType;
    this.cardCode = cardCode;
  }
}
