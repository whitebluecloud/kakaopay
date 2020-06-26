package com.kakaopay.spread.service;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;
import com.kakaopay.spread.domain.SpreadTicketFactory;
import com.kakaopay.spread.dto.spread.SpreadRequestDto;
import com.kakaopay.spread.repository.DivideSpreadMoneyRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

@Service
@Slf4j
public class SpreadMoneyService {

  @Autowired
  private DivideSpreadMoneyRepository divideSpreadMoneyRepository;

  public SpreadTicket spreadMoney(SpreadRequestDto spreadRequestDto, long userId, String roomId) {
    String token = makeToken();
    List<DivideSpreadMoney> divideSpreadMoneyList = divideMoney(spreadRequestDto, token);
    log.info("token : {} divideSpreadMoneyList : {}", token, divideSpreadMoneyList);
    SpreadTicket spreadTicket = SpreadTicketFactory.create(spreadRequestDto, token, userId, roomId, divideSpreadMoneyList);
    log.info("spreadTicket create : {}", spreadTicket);
    return spreadTicket;
  }

  private List<DivideSpreadMoney> divideMoney(SpreadRequestDto spreadRequestDto, String token) {
    if (!isValidRequst(spreadRequestDto)) {
      throw new RuntimeException();
    }

    ArrayList<Long> moneyList = new ArrayList<>();
    long amount = spreadRequestDto.getAmount();
    int headCount = spreadRequestDto.getHeadCount();
    do {
      if (headCount == 1) {
        moneyList.add(amount);
        break;
      }
      long randomAmount = (long) (Math.random() * amount / headCount) + 1;
      moneyList.add(randomAmount);
      amount -= randomAmount;
      headCount--;
    }while (headCount > 0);

    List<DivideSpreadMoney> divideSpreadMoneyList = moneyList.stream()
      .map(money -> DivideSpreadMoney.builder().amount(money).token(token).receiveUserId("").build())
      .collect(Collectors.toList());
    List<DivideSpreadMoney> savedDivideSpreadMoneyList = divideSpreadMoneyRepository.saveAll(divideSpreadMoneyList);
    return savedDivideSpreadMoneyList;
  }

  private boolean isValidRequst(SpreadRequestDto spreadRequestDto) {
    return true;
  }

  private String makeToken() {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < 3; i++){
      if(random.nextBoolean()){
        sb.append((char)(random.nextInt(26) +97));
      }else{
        sb.append((random.nextInt(10)));
      }
    }
    return sb.toString();
  }
}
