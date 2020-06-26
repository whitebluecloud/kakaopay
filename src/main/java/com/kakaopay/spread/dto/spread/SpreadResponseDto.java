package com.kakaopay.spread.dto.spread;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
  private LocalDateTime publishDate;
  private long receivedAmount;
  private List<DivideSpreadMoney> divideSpreadMoneyList;

  public static SpreadResponseDto of(SpreadTicket spreadTicket) {
    return SpreadResponseDto.builder()
      .token(spreadTicket.getToken())
      .amount(spreadTicket.getAmount())
      .receivedAmount(
        spreadTicket
          .getDivideSpreadMoneyList()
          .stream()
          .filter(divideSpreadMoney -> divideSpreadMoney.getReceiveUserId() != 0)
          .map(DivideSpreadMoney::getAmount)
          .mapToLong(Long::longValue)
          .sum()
      )
      .headCount(spreadTicket.getHeadCount())
      .divideSpreadMoneyList(spreadTicket.getDivideSpreadMoneyList())
      .publishDate(spreadTicket.getPublishDate())
      .build();
  }
}
