package com.kakaopay.spread.service;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.SpreadTicket;
import com.kakaopay.spread.repository.DivideSpreadMoneyRepository;
import com.kakaopay.spread.repository.SpreadTicketRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpreadMoneyServiceTest {

  @InjectMocks
  private SpreadMoneyService spreadMoneyService;

  @Mock
  private SpreadTicketRepository spreadTicketRepository;

  @Mock
  private DivideSpreadMoneyRepository divideSpreadMoneyRepository;

  private SpreadTicket expiredSpreadTicket;
  private SpreadTicket notExpiredSpreadTicket;
  private SpreadTicket user1CreatedSpreadTicket;

  @Before
  public void setup() {
    expiredSpreadTicket = SpreadTicket.builder().roomId("b").token("err").publishDate(LocalDateTime.now().minusDays(8)).build();
    notExpiredSpreadTicket = SpreadTicket.builder().publishUserId(1).roomId("a").token("abc").publishDate(LocalDateTime.now().minusDays(6)).build();
    user1CreatedSpreadTicket = SpreadTicket.builder().publishUserId(1).roomId("a").token("def").publishDate(LocalDateTime.now()).build();
  }

  @Test
  public void 조회_API_정상_기한_토큰_조회_테스트() {
    given(spreadTicketRepository.findByTokenAndRoomId("abc", "a")).willReturn(notExpiredSpreadTicket);
    when(spreadMoneyService.getSpreadTicket("abc", 1, "a")).thenReturn(notExpiredSpreadTicket);
  }

  @Test(expected = RuntimeException.class)
  public void 조회_API_만료된_토큰_조회_테스트() {
    given(spreadTicketRepository.findByTokenAndRoomId("err", "b")).willReturn(expiredSpreadTicket);
    when(spreadMoneyService.getSpreadTicket("err", 1, "b")).thenThrow(new RuntimeException("해당 뿌리기는 만료되었습니다."));
  }

  @Test
  public void 조회_API_토큰_생성자_호출_조회_테스트() {
    given(spreadTicketRepository.findByTokenAndRoomId("def", "a")).willReturn(user1CreatedSpreadTicket);
    when(spreadMoneyService.getSpreadTicket("def", 1, "a")).thenReturn(user1CreatedSpreadTicket);
  }

  @Test(expected = RuntimeException.class)
  public void 조회_API_토큰_비생성자_호출_조회_테스트() {
    given(spreadTicketRepository.findByTokenAndRoomId("def", "a")).willReturn(user1CreatedSpreadTicket);
    when(spreadMoneyService.getSpreadTicket("def", 2, "a")).thenThrow(new RuntimeException("본인의 뿌리기 정보만 볼 수 있습니다."));
  }

  @Test(expected = RuntimeException.class)
  public void 받기_API_한사용자_두번_받기_시도_테스트() {
    SpreadTicket spreadTicket = SpreadTicket.builder().publishUserId(1).roomId("a").token("abc").amount(1000).publishDate(LocalDateTime.now()).headCount(2).build();
    List<DivideSpreadMoney> alreadyUser2ReceivedSpreadMoneyList = List.of(DivideSpreadMoney.builder().receiveUserId(2).amount(500).build(), DivideSpreadMoney.builder().amount(500).build());
    given(spreadTicketRepository.findByTokenAndRoomId("abc", "a")).willReturn(user1CreatedSpreadTicket);
    given(divideSpreadMoneyRepository.findAllByToken("abc")).willReturn(alreadyUser2ReceivedSpreadMoneyList);

    when(spreadMoneyService.receiveMoney(2, "a", "abc")).thenThrow(new RuntimeException("이미 받은 뿌리기 입니다."));
  }
}