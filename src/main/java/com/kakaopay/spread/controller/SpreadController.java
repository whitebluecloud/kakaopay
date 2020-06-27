package com.kakaopay.spread.controller;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;
import com.kakaopay.spread.dto.spread.*;
import com.kakaopay.spread.service.CreateSpreadService;
import com.kakaopay.spread.service.ReceiveSpreadService;
import com.kakaopay.spread.service.SearchSpreadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class SpreadController {

  @Autowired
  private CreateSpreadService createSpreadService;

  @Autowired
  private ReceiveSpreadService receiveSpreadService;

  @Autowired
  private SearchSpreadService searchSpreadService;

  @PostMapping("/spread")
  public SpreadResponseDto createSpread(
    @RequestHeader(name = "X-USER-ID") long userId,
    @RequestHeader(name = "X-ROOM-ID") String roomId,
    @RequestBody SpreadCreateReqDto spreadCreateReqDto) {

    SpreadResponseDto spreadResponseDto =
      createSpreadService.spreadMoney(
        CreateSpreadDto.builder()
          .userId(userId)
          .roomId(roomId)
          .amount(spreadCreateReqDto.getAmount())
          .headCount(spreadCreateReqDto.getHeadCount())
          .build()
      );
    log.info("createSpread result : {}", spreadResponseDto);
    return spreadResponseDto;
  }

  @PutMapping("/spread/{token}")
  public void receiveSpread(@RequestHeader(name = "X-USER-ID") long userId,
                            @RequestHeader(name = "X-ROOM-ID") String roomId,
                            @PathVariable(name = "token") String token) {
    receiveSpreadService.receiveMoney(ReceiveSpreadDto.builder().userId(userId).roomId(roomId).token(token).build());
  }

  @GetMapping("/spread/{token}")
  public SpreadResponseDto searchSpread(@RequestHeader(name = "X-USER-ID") long userId,
                                        @RequestHeader(name = "X-ROOM-ID") String roomId,
                                        @PathVariable(name = "token") String token) {
    SpreadTicket spreadTicket = searchSpreadService.getSpreadTicket(SearchSpreadDto.builder().userId(userId).roomId(roomId).token(token).build());
    List<DivideSpreadMoney> divideSpreadMoneyList = searchSpreadService.getDivideSpreadMoneyList(token);
    return SpreadResponseDto.of(spreadTicket, divideSpreadMoneyList);
  }
}
