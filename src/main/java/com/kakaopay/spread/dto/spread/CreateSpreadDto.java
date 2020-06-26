package com.kakaopay.spread.dto.spread;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CreateSpreadDto {
  private long userId;
  private String roomId;
  private long amount;
  private int headCount;
}
