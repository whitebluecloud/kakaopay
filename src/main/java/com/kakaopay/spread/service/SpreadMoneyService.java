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
      throw new RuntimeException("이미 받은 뿌리기 입니다.");
    }

    boolean isOwnSpreadMoney = userId == spreadTicket.getPublishUserId();
    if (isOwnSpreadMoney) {
      throw new RuntimeException("자신이 뿌린 뿌리기는 받을 수 없습니다.");
    }

    boolean isNotRoomMember = !spreadTicket.getRoomId().equalsIgnoreCase(roomId);
    if (isNotRoomMember) {
      throw new RuntimeException("다른 대화방 사용자는 해당 뿌리기를 받을 수 없습니다.");
    }

    boolean isReceiveExpired= spreadTicket.getPublishDate().plusMinutes(10).isBefore(LocalDateTime.now());
    if (isReceiveExpired) {
      throw new RuntimeException("만료된 뿌리기 입니다.");
    }

    DivideSpreadMoney divideSpreadMoney = divideSpreadMoneyList.stream()
      .filter(DivideSpreadMoney::isNotReceived)
      .findAny()
      .orElseThrow(() -> new RuntimeException("받을 수 있는 뿌리기가 없습니다."));

    divideSpreadMoney.setReceiveUserId(userId);
    DivideSpreadMoney receivedDivideSpreadMoney = divideSpreadMoneyRepository.save(divideSpreadMoney);

    log.info("receivedDivideSpreadMoney : {}", receivedDivideSpreadMoney);
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
      throw new RuntimeException("유효한 뿌리기 정보가 아닙니다.");
    }

    if (isNotOwnSpreadTicket(spreadTicket, userId)) {
      throw new RuntimeException("본인의 뿌리기 정보만 볼 수 있습니다.");
    }

    if (spreadTicket.isExpired(LocalDateTime.now())) {
      throw new RuntimeException("해당 뿌리기는 만료되었습니다.");
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