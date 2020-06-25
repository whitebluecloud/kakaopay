package com.kakaopay.spread.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@ToString
@Document("room")
public class Room {

  @Id
  private String roomId;

  private List<User> users;
}
