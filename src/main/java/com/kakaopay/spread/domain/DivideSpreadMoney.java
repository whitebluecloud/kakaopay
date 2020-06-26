package com.kakaopay.spread.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@ToString
@Document("divideSpreadMoney")
public class DivideSpreadMoney {
  @Indexed
  private String token;
  private long amount;
  private long receiveUserId;

  public boolean isNotReceived() {
    return receiveUserId == 0;
  }
}
