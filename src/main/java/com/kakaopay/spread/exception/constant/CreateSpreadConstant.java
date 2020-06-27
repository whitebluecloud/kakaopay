package com.kakaopay.spread.exception.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CreateSpreadConstant {

  유효하지_않은_요청("유효하지 않은 요청입니다."),
  ;

  private String msg;
}
