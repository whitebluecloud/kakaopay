package com.kakaopay.spread.service;

import com.kakaopay.spread.dto.SpreadDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

@Service
@Slf4j
public class SpreadMoneyService {

  public void spreadMoney(SpreadDto spreadDto, long userId, String roomId) {
    ArrayList<Long> dividedMoney = divideMoney(spreadDto);
    String token = makeToken();
    log.info("{}", token);
  }

  private ArrayList<Long> divideMoney(SpreadDto spreadDto) {
    if (!isValidRequst(spreadDto)) {
      throw new RuntimeException();
    }

    ArrayList<Long> dividedMoney = new ArrayList<>();
    long amount = spreadDto.getAmount();
    int headCount = spreadDto.getHeadCount();
    do {
      if (headCount == 1) {
        dividedMoney.add(amount);
        break;
      }
      long randomAmount = (long) (Math.random() * amount / headCount) + 1;
      dividedMoney.add(randomAmount);
      amount -= randomAmount;
      headCount--;
    }while (headCount > 0);

    return dividedMoney;
  }

  private boolean isValidRequst(SpreadDto spreadDto) {
    return true;
  }

  private String makeToken() {
    Random random = new Random();
    StringBuffer buf = new StringBuffer();
    for(int i = 0; i < 3; i++){
      if(random.nextBoolean()){
        buf.append((char)(random.nextInt(26) +97));
      }else{
        buf.append((random.nextInt(10)));
      }
    }
    return buf.toString();
  }
}
