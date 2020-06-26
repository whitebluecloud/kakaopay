package com.kakaopay.spread.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@ToString
@Document("divideSpreadMoney")
public class DivideSpreadMoney {
  private String token;
  private long amount;
  private long receiveUserId;
}
