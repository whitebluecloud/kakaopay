package com.kakaopay.spread.dto.spread;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SpreadResponseDto {

  private String token;
  private long amount;
  private long headCount;
  private List<DivideSpreadMoney> divideSpreadMoneyList;

  public static SpreadResponseDto of(SpreadTicket spreadTicket) {
    return SpreadResponseDto.builder()
      .token(spreadTicket.getToken())
      .amount(spreadTicket.getAmount())
      .headCount(spreadTicket.getHeadCount())
      .divideSpreadMoneyList(spreadTicket.getDivideSpreadMoneyList())
      .build();
  }
}
