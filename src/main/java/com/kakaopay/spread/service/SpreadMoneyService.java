package com.kakaopay.spread.service;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;
import com.kakaopay.spread.domain.SpreadTicketFactory;
import com.kakaopay.spread.dto.spread.SpreadRequestDto;
import com.kakaopay.spread.exception.NotAllowedGetTicketInfoException;
import com.kakaopay.spread.exception.NotAllowedReceiveException;
import com.kakaopay.spread.exception.constant.NotAllowedGetTicketInfoConstant;
import com.kakaopay.spread.exception.constant.NotAllowedReceiveConstant;
import com.kakaopay.spread.repository.DivideSpreadMoneyRepository;
import com.kakaopay.spread.repository.SpreadTicketRepository;

import java.time.LocalDateTime;
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
  private SpreadTicketRepository spreadTicketRepository;

  @Autowired
  private DivideSpreadMoneyRepository divideSpreadMoneyRepository;

  public SpreadTicket spreadMoney(SpreadRequestDto spreadRequestDto, long userId, String roomId) {
    String token = makeToken();
    log.info("token : {}", token);

    SpreadTicket spreadTicket = SpreadTicketFactory.create(spreadRequestDto, token, userId, roomId);
    SpreadTicket savedSpreadTicket = spreadTicketRepository.save(spreadTicket);
    log.info("spreadTicket save: {}", savedSpreadTicket);

    List<DivideSpreadMoney> divideSpreadMoneyList = divideMoney(spreadRequestDto, token);
    divideSpreadMoneyRepository.saveAll(divideSpreadMoneyList);
    log.info("divideSpreadMoneyList : {}", divideSpreadMoneyList);

    return spreadTicket;
  }

  public DivideSpreadMoney receiveMoney(long userId, String roomId, String token) {

    SpreadTicket spreadTicket = spreadTicketRepository.findByTokenAndRoomId(token, roomId);

    List<DivideSpreadMoney> divideSpreadMoneyList = divideSpreadMoneyRepository.findAllByToken(token);

    boolean isAlreadyReceived = divideSpreadMoneyList.stream().anyMatch(money -> userId == money.getReceiveUserId());
    if (isAlreadyReceived) {
      throw new NotAllowedReceiveException(NotAllowedReceiveConstant.이미_받은_뿌리기_받기_에러.getMsg());
    }

    boolean isOwnSpreadMoney = userId == spreadTicket.getPublishUserId();
    if (isOwnSpreadMoney) {
      throw new NotAllowedReceiveException(NotAllowedReceiveConstant.본인_뿌리기_받기_에러.getMsg());
    }

    boolean isNotRoomMember = !spreadTicket.getRoomId().equalsIgnoreCase(roomId);
    if (isNotRoomMember) {
      throw new NotAllowedReceiveException(NotAllowedReceiveConstant.타_대화방_멤버_받기_에러.getMsg());
    }

    boolean isReceiveExpired= spreadTicket.getPublishDate().plusMinutes(10).isBefore(LocalDateTime.now());
    if (isReceiveExpired) {
      throw new NotAllowedReceiveException(NotAllowedReceiveConstant.만료된_뿌리기_받기_에러.getMsg());
    }

    DivideSpreadMoney divideSpreadMoney = divideSpreadMoneyList.stream()
      .filter(DivideSpreadMoney::isNotReceived)
      .findAny()
      .orElseThrow(() -> new RuntimeException(NotAllowedReceiveConstant.다_받은_뿌리기_받기_에러.getMsg()));

    divideSpreadMoney.setReceiveUserId(userId);
    DivideSpreadMoney receivedDivideSpreadMoney = divideSpreadMoneyRepository.save(divideSpreadMoney);
    return receivedDivideSpreadMoney;
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
    } while (headCount > 0);

    List<DivideSpreadMoney> divideSpreadMoneyList = moneyList.stream()
      .map(money -> DivideSpreadMoney.builder().amount(money).token(token).build())
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
    for (int i = 0; i < 3; i++) {
      if (random.nextBoolean()) {
        sb.append((char) (random.nextInt(26) + 97));
      } else {
        sb.append((random.nextInt(10)));
      }
    }
    return sb.toString();
  }

  public SpreadTicket getSpreadTicket(String token, long userId, String roomId) {
    SpreadTicket spreadTicket = spreadTicketRepository.findByTokenAndRoomId(token, roomId);
    if (spreadTicket == null) {
      throw new NotAllowedGetTicketInfoException(NotAllowedGetTicketInfoConstant.유효하지않은_티켓_조회_에러.getMsg());
    }
    if (isNotOwnSpreadTicket(spreadTicket, userId)) {
      throw new NotAllowedGetTicketInfoException(NotAllowedGetTicketInfoConstant.타인_뿌리기_조회_에러.getMsg());
    }
    if (spreadTicket.isExpired(LocalDateTime.now())) {
      throw new NotAllowedGetTicketInfoException(NotAllowedGetTicketInfoConstant.만료된_티켓_조회_에러.getMsg());
    }
    return spreadTicket;
  }

  public List<DivideSpreadMoney> getDivideSpreadMoneyList(String token) {
    // TODO 예외처리
    return divideSpreadMoneyRepository.findAllByToken(token);
  }

  private boolean isNotOwnSpreadTicket(SpreadTicket spreadTicket, long userId) {
    return spreadTicket.getPublishUserId() != userId;
  }
}