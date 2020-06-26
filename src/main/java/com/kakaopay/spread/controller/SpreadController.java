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

  /**
   * TODO 뿌리기 시 발급된 token을 요청값으로 받습니다.
   * TODO token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.
   * TODO 뿌리기 당 한 사용자는 한번만 받을 수 있습니다. TODO 자신이 뿌리기한 건은 자신이 받을 수 없습니다.
   * TODO 뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.
   * TODO 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.
   */
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
