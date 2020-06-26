package com.kakaopay.spread.dto.spread;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SpreadRequestDto {
  private long amount;
  private int headCount;
}
