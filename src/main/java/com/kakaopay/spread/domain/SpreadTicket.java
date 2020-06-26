package com.kakaopay.spread.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@ToString
@Document("spreadTicket")
public class SpreadTicket {

  private String roomId;
  private String token;
  private long publishUserId;
  private long amount;
  private long headCount;
  private LocalDateTime publishDate;
  private List<DivideSpreadMoney> divideSpreadMoneyList;

  public boolean isExpired(LocalDateTime now) {
    return publishDate.plusDays(7).isBefore(now);
  }
}
