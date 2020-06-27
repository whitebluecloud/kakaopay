package com.kakaopay.spread.service;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.dto.spread.CreateSpreadDto;
import com.kakaopay.spread.dto.spread.SpreadResponseDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateSpreadServiceTest {

  @Autowired
  private CreateSpreadService createSpreadService;

  @Before
  public void setup() {
  }

  @Test
  public void 뿌리기_API_토큰_생성_테스트() {

  }

  @Test
  public void 뿌리기_API_분배_테스트() {
    final int HEAD_COUNT = 10;
    SpreadResponseDto spreadResponseDto = createSpreadService.spreadMoney(CreateSpreadDto.builder().userId(1).roomId("a").amount(1000).headCount(HEAD_COUNT).build());
    Assert.assertThat(spreadResponseDto.getDivideSpreadMoneyList().size(),is(HEAD_COUNT));
  }

  @Test
  public void 뿌리기_API_분배금액_검증_테스트() {
    final long AMOUNT = 1000;
    final int HEAD_COUNT = 10;
    SpreadResponseDto spreadResponseDto = createSpreadService.spreadMoney(CreateSpreadDto.builder().userId(1).roomId("a").amount(AMOUNT).headCount(HEAD_COUNT).build());
    long summarizedAmount = spreadResponseDto.getDivideSpreadMoneyList().stream()
      .map(DivideSpreadMoney::getAmount)
      .mapToLong(Long::longValue)
      .sum();

    Assert.assertThat(summarizedAmount, is(AMOUNT));
  }

}