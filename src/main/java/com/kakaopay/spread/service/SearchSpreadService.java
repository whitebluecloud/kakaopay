package com.kakaopay.spread.service;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;
import com.kakaopay.spread.dto.spread.SearchSpreadDto;
import com.kakaopay.spread.exception.NotAllowedGetTicketInfoException;
import com.kakaopay.spread.exception.constant.NotAllowedGetTicketInfoConstant;
import com.kakaopay.spread.repository.DivideSpreadMoneyRepository;
import com.kakaopay.spread.repository.SpreadTicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class SearchSpreadService {

  @Autowired
  private SpreadTicketRepository spreadTicketRepository;

  @Autowired
  private DivideSpreadMoneyRepository divideSpreadMoneyRepository;

  public SpreadTicket getSpreadTicket(SearchSpreadDto searchSpreadDto) {
    SpreadTicket spreadTicket = spreadTicketRepository.findByTokenAndRoomId(searchSpreadDto.getToken(), searchSpreadDto.getRoomId());
    if (spreadTicket == null) {
      throw new NotAllowedGetTicketInfoException(NotAllowedGetTicketInfoConstant.유효하지않은_티켓_조회_에러.getMsg());
    }
    if (isNotOwnSpreadTicket(spreadTicket, searchSpreadDto.getUserId())) {
      throw new NotAllowedGetTicketInfoException(NotAllowedGetTicketInfoConstant.타인_뿌리기_조회_에러.getMsg());
    }
    if (spreadTicket.isExpired(LocalDateTime.now())) {
      throw new NotAllowedGetTicketInfoException(NotAllowedGetTicketInfoConstant.만료된_티켓_조회_에러.getMsg());
    }
    return spreadTicket;
  }

  private boolean isNotOwnSpreadTicket(SpreadTicket spreadTicket, long userId) {
    return spreadTicket.getPublishUserId() != userId;
  }

  public List<DivideSpreadMoney> getDivideSpreadMoneyList(String token) {
    // TODO 예외처리
    return divideSpreadMoneyRepository.findAllByToken(token);
  }
}