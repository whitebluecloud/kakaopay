package com.kakaopay.spread.domain;

import java.time.LocalDateTime;
import java.util.List;

public class SpreadTicket {

  private String token;
  private String publishUserId;
  private String roomId;
  private LocalDateTime publishDate;

  private List<DivideMoney> divideMoneyList;

  private class DivideMoney {
    private long amount;
    private String receiveUserId;
  }
}
