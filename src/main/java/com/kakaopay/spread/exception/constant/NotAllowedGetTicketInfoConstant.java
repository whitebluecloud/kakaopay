package com.kakaopay.spread.exception.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotAllowedGetTicketInfoConstant {

  유효하지않은_티켓_조회_에러("유효한 뿌리기 정보가 아닙니다."),
  타인_뿌리기_조회_에러("본인의 뿌리기 정보만 볼 수 있습니다."),
  만료된_티켓_조회_에러("해당 뿌리기는 만료되었습니다.")
  ;

  private String msg;
}
