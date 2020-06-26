package com.kakaopay.spread.service;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;
import com.kakaopay.spread.domain.SpreadTicketFactory;
import com.kakaopay.spread.dto.spread.SpreadRequestDto;
import com.kakaopay.spread.repository.DivideSpreadMoneyRepository;
import com.kakaopay.spread.repository.SpreadTicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
  private SpreadTicketRepository spreadTicketRepository;

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

    List<Long> moneyList = new ArrayList<>();
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

  public SpreadTicket getSpreadTicket(String token, long userId, String roomId) {

    SpreadTicket spreadTicket = spreadTicketRepository.findByTokenAndRoomId(token, roomId);

    if (spreadTicket == null) {
      throw new RuntimeException("유효한 뿌리기 정보가 아닙니다.");
    }

    if (isOwnSpreadTicket(spreadTicket, userId)) {
      throw new RuntimeException("본인의 뿌리기 정보만 볼 수 있습니다.");
    }

    if (spreadTicket.isExpired(LocalDateTime.now())) {
      throw new RuntimeException("해당 뿌리기는 만료되었습니다.");
    }

    return spreadTicket;
  }

  private boolean isOwnSpreadTicket(SpreadTicket spreadTicket, long userId) {
    return spreadTicket.getPublishUserId() == userId;
  }
}
