package com.kakaopay.spread.dto.spread;

import com.kakaopay.spread.domain.SpreadTicket;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SpreadResponseDto {

  private long amount;
  private long headCount;

  public static SpreadResponseDto of(SpreadTicket spreadTicket) {
    return SpreadResponseDto.builder()
      .amount(spreadTicket.getAmount())
      .headCount(spreadTicket.getHeadCount())
      .build();
  }
}
