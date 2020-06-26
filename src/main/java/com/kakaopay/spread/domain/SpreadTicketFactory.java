package com.kakaopay.spread.domain;

import com.kakaopay.spread.dto.spread.CreateSpreadDto;

import java.time.LocalDateTime;
import java.util.Random;

public class SpreadTicketFactory {
  public static SpreadTicket create(CreateSpreadDto createSpreadDto, Random random) {
    return SpreadTicket.builder()
      .amount(createSpreadDto.getAmount())
      .headCount(createSpreadDto.getHeadCount())
      .roomId(createSpreadDto.getRoomId())
      .publishUserId(createSpreadDto.getUserId())
      .token(makeToken(random))
      .publishDate(LocalDateTime.now())
      .build();
  }

  private static String makeToken(Random random) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 3; i++) {
      if (random.nextBoolean()) {
        sb.append((char) (random.nextInt(26) + 97));
      } else {
        sb.append((random.nextInt(10)));
      }
    }
    return sb.toString();
  }
}
