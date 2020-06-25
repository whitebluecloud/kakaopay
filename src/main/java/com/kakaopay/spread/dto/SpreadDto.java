package com.kakaopay.spread.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SpreadDto {
  private long amount;
  private int headCount;
}
