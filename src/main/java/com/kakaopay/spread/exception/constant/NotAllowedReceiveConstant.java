package com.kakaopay.spread.exception.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotAllowedReceiveConstant {

  이미_받은_뿌리기_받기_에러("이미 받은 뿌리기 입니다."),
  만료된_뿌리기_받기_에러("만료된 뿌리기 입니다."),
  타_대화방_멤버_받기_에러("해당 뿌리기가 호출된 방에 속한 사용자만이 받을 수 없습니다."),
  본인_뿌리기_받기_에러("자신이 뿌린 뿌리기는 받을 수 없습니다."),
  다_받은_뿌리기_받기_에러("받을 수 있는 뿌리기가 없습니다.")
  ;

  private String msg;
}
