package com.kakaopay.spread.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@ToString
@Document("user")
public class User {

  @Id
  private long userId;

  private long balance;
}
