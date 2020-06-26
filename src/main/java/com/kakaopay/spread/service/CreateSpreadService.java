package com.kakaopay.spread.service;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;
import com.kakaopay.spread.domain.SpreadTicketFactory;
import com.kakaopay.spread.dto.spread.CreateSpreadDto;
import com.kakaopay.spread.repository.DivideSpreadMoneyRepository;
import com.kakaopay.spread.repository.SpreadTicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CreateSpreadService {

  @Autowired
  private SpreadTicketRepository spreadTicketRepository;

  @Autowired
  private DivideSpreadMoneyRepository divideSpreadMoneyRepository;

  public SpreadTicket spreadMoney(CreateSpreadDto createSpreadDto) {
    SpreadTicket spreadTicket = SpreadTicketFactory.create(createSpreadDto, new Random());
    SpreadTicket savedSpreadTicket = spreadTicketRepository.save(spreadTicket);
    log.info("spreadTicket save: {}", savedSpreadTicket);

    List<DivideSpreadMoney> divideSpreadMoneyList = divideMoney(spreadTicket);
    divideSpreadMoneyRepository.saveAll(divideSpreadMoneyList);
    log.info("divideSpreadMoneyList : {}", divideSpreadMoneyList);

    return spreadTicket;
  }

  private List<DivideSpreadMoney> divideMoney(SpreadTicket spreadTicket) {
    if (!isValidRequst(spreadTicket)) {
      throw new RuntimeException();
    }

    List<Long> moneyList = new ArrayList<>();
    long amount = spreadTicket.getAmount();
    long headCount = spreadTicket.getHeadCount();
    do {
      if (headCount == 1) {
        moneyList.add(amount);
        break;
      }
      long randomAmount = (long) (Math.random() * amount / headCount) + 1;
      moneyList.add(randomAmount);
      amount -= randomAmount;
      headCount--;
    } while (headCount > 0);

    List<DivideSpreadMoney> divideSpreadMoneyList = moneyList.stream()
      .map(money -> DivideSpreadMoney.builder().amount(money).token(spreadTicket.getToken()).build())
      .collect(Collectors.toList());
    List<DivideSpreadMoney> savedDivideSpreadMoneyList = divideSpreadMoneyRepository.saveAll(divideSpreadMoneyList);
    return savedDivideSpreadMoneyList;
  }

  private boolean isValidRequst(SpreadTicket spreadTicket) {
    // TODO 파라미터 체크
    return true;
  }
}