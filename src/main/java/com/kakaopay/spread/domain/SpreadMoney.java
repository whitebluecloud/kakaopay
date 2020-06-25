package com.kakaopay.spread.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@ToString
@Document("spreadMoney")
public class SpreadMoney {

  @Id
  private String token;

  private long balance;
}
