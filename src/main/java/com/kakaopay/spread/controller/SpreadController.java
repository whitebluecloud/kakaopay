package com.kakaopay.spread.controller;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;
import com.kakaopay.spread.dto.spread.SpreadRequestDto;
import com.kakaopay.spread.dto.spread.SpreadResponseDto;
import com.kakaopay.spread.repository.RoomRepository;
import com.kakaopay.spread.service.SpreadMoneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class SpreadController {

  @Autowired
  private SpreadMoneyService spreadMoneyService;

  @PostMapping("/spread")
  public SpreadResponseDto spreadMoney(
    @RequestHeader(name = "X-USER-ID") long userId,
    @RequestHeader(name = "X-ROOM-ID") String roomId,
    @RequestBody SpreadRequestDto spreadRequestDto) {

    SpreadResponseDto spreadResponseDto = SpreadResponseDto.of(spreadMoneyService.spreadMoney(spreadRequestDto, userId, roomId));
    log.info("spread result : {}", spreadResponseDto);
    return spreadResponseDto;
  }

  @PutMapping("/spread/{token}")
  public void receiveSpreadMoney(@RequestHeader(name = "X-USER-ID") long userId,
                                 @RequestHeader(name = "X-ROOM-ID") String roomId,
                                 @PathVariable(name = "token") String token) {
    spreadMoneyService.receiveMoney(userId, roomId, token);
  }

  @GetMapping("/spread/{token}")
  public SpreadResponseDto getSpreadInfo(@RequestHeader(name = "X-USER-ID") long userId,
                                         @RequestHeader(name = "X-ROOM-ID") String roomId,
                                         @PathVariable(name = "token") String token) {
    SpreadTicket spreadTicket = spreadMoneyService.getSpreadTicket(token, userId, roomId);
    List<DivideSpreadMoney> divideSpreadMoneyList = spreadMoneyService.getDivideSpreadMoneyList(token);
    return SpreadResponseDto.of(spreadTicket, divideSpreadMoneyList);
  }
}
