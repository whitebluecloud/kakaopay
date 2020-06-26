package com.kakaopay.spread.dto.spread;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReceiveSpreadDto {
  private long userId;
  private String roomId;
  private String token;
}
