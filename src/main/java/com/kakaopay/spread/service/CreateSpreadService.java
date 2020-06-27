package com.kakaopay.spread.service;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;
import com.kakaopay.spread.domain.SpreadTicketFactory;
import com.kakaopay.spread.dto.spread.CreateSpreadDto;
import com.kakaopay.spread.dto.spread.SpreadResponseDto;
import com.kakaopay.spread.exception.CreateSpreadException;
import com.kakaopay.spread.exception.constant.CreateSpreadConstant;
import com.kakaopay.spread.repository.DivideSpreadMoneyRepository;
import com.kakaopay.spread.repository.RoomRepository;
import com.kakaopay.spread.repository.SpreadTicketRepository;
import com.kakaopay.spread.repository.UserRepository;
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
  private UserRepository userRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private DivideSpreadMoneyRepository divideSpreadMoneyRepository;

  public SpreadResponseDto spreadMoney(CreateSpreadDto createSpreadDto) throws CreateSpreadException {

    if (userRepository.findById(createSpreadDto.getUserId()).isEmpty()) {
      throw new CreateSpreadException(CreateSpreadConstant.유효하지_않은_요청.getMsg());
    }

    if (roomRepository.findById(createSpreadDto.getRoomId()).isEmpty()) {
      throw new CreateSpreadException(CreateSpreadConstant.유효하지_않은_요청.getMsg());
    }

    SpreadTicket spreadTicket = SpreadTicketFactory.create(createSpreadDto, new Random());
    SpreadTicket savedSpreadTicket = spreadTicketRepository.save(spreadTicket);
    log.info("spreadTicket save: {}", savedSpreadTicket);

    List<DivideSpreadMoney> divideSpreadMoneyList = divideMoney(spreadTicket);
    divideSpreadMoneyRepository.saveAll(divideSpreadMoneyList);
    log.info("divideSpreadMoneyList : {}", divideSpreadMoneyList);

    SpreadResponseDto spreadResponseDto = SpreadResponseDto.of(spreadTicket, divideSpreadMoneyList);
    return spreadResponseDto;
  }

  private List<DivideSpreadMoney> divideMoney(SpreadTicket spreadTicket) {

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


}