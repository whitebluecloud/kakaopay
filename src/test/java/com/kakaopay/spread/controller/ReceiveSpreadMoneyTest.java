package com.kakaopay.spread.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.dto.spread.SpreadResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReceiveSpreadMoneyTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private SpreadController spreadController;

  @Before
  public void setup() {
  }

  @Test
  public void 조회_API_테스트() throws Exception {
    SpreadResponseDto spreadResponseDto =
      SpreadResponseDto.builder()
        .token("abc")
        .receivedAmount(1000L)
        .divideSpreadMoneyList(List.of(
          DivideSpreadMoney.builder().receiveUserId(1).amount(450L).build(),
          DivideSpreadMoney.builder().receiveUserId(2).amount(300L).build(),
          DivideSpreadMoney.builder().receiveUserId(3).amount(250L).build()
          )
        )
        .publishDate(LocalDateTime.now())
        .build();
    given(spreadController.getSpreadInfo(1, "a", "abc")).willReturn(spreadResponseDto);

    mockMvc.perform(get("/spread/abc")
      .header("X-USER-ID", 1)
      .header("X-ROOM-ID", "a")
    ).andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(spreadResponseDto)))
      .andDo(print());
  }

  @Test
  public void 만료된_토큰_조회_테스트() throws Exception {
    SpreadResponseDto spreadResponseDto =
      SpreadResponseDto.builder()
        .token("err")
        .receivedAmount(1000L)
        .divideSpreadMoneyList(List.of(
          DivideSpreadMoney.builder().receiveUserId(1).amount(450L).build(),
          DivideSpreadMoney.builder().receiveUserId(2).amount(300L).build(),
          DivideSpreadMoney.builder().receiveUserId(3).amount(250L).build()
          )
        )
        .publishDate(LocalDateTime.now().minusDays(8))
        .build();

    given(spreadController.getSpreadInfo(2, "b", "err")).willThrow();

    mockMvc.perform(get("/spread/err")
      .header("X-USER-ID", 2)
      .header("X-ROOM-ID", "b")
    ).andExpect(status().is4xxClientError())
      .andDo(print());
  }
}
