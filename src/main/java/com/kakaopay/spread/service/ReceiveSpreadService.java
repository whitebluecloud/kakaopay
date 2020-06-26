package com.kakaopay.spread.service;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;
import com.kakaopay.spread.dto.spread.ReceiveSpreadDto;
import com.kakaopay.spread.exception.NotAllowedReceiveException;
import com.kakaopay.spread.exception.constant.NotAllowedReceiveConstant;
import com.kakaopay.spread.repository.DivideSpreadMoneyRepository;
import com.kakaopay.spread.repository.SpreadTicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ReceiveSpreadService {

  @Autowired
  private SpreadTicketRepository spreadTicketRepository;

  @Autowired
  private DivideSpreadMoneyRepository divideSpreadMoneyRepository;

  public DivideSpreadMoney receiveMoney(ReceiveSpreadDto receiveSpreadDto) {

    SpreadTicket spreadTicket = spreadTicketRepository.findByTokenAndRoomId(receiveSpreadDto.getToken(), receiveSpreadDto.getRoomId());

    List<DivideSpreadMoney> divideSpreadMoneyList = divideSpreadMoneyRepository.findAllByToken(receiveSpreadDto.getToken());

    boolean isAlreadyReceived = divideSpreadMoneyList.stream().anyMatch(money -> receiveSpreadDto.getUserId()== money.getReceiveUserId());
    if (isAlreadyReceived) {
      throw new NotAllowedReceiveException(NotAllowedReceiveConstant.이미_받은_뿌리기_받기_에러.getMsg());
    }

    boolean isOwnSpreadMoney = receiveSpreadDto.getUserId() == spreadTicket.getPublishUserId();
    if (isOwnSpreadMoney) {
      throw new NotAllowedReceiveException(NotAllowedReceiveConstant.본인_뿌리기_받기_에러.getMsg());
    }

    boolean isNotRoomMember = !spreadTicket.getRoomId().equalsIgnoreCase(receiveSpreadDto.getRoomId());
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

    divideSpreadMoney.setReceiveUserId(receiveSpreadDto.getUserId());
    DivideSpreadMoney receivedDivideSpreadMoney = divideSpreadMoneyRepository.save(divideSpreadMoney);
    return receivedDivideSpreadMoney;
  }
}