package com.kakaopay.spread.domain;

import com.kakaopay.spread.dto.spread.SpreadRequestDto;
import java.time.LocalDateTime;
import java.util.List;

public class SpreadTicketFactory {
  public static SpreadTicket create(SpreadRequestDto spreadRequestDto, String token, long userId, String roomId) {
    return SpreadTicket.builder()
      .amount(spreadRequestDto.getAmount())
      .headCount(spreadRequestDto.getHeadCount())
      .roomId(roomId)
      .token(token)
      .publishUserId(userId)
      .publishDate(LocalDateTime.now())
      .build();
  }
}
