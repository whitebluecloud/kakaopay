package com.kakaopay.spread.service;

import com.kakaopay.spread.domain.SpreadTicket;
import com.kakaopay.spread.dto.spread.SearchSpreadDto;
import com.kakaopay.spread.exception.NotAllowedGetTicketInfoException;
import com.kakaopay.spread.exception.constant.NotAllowedGetTicketInfoConstant;
import com.kakaopay.spread.repository.DivideSpreadMoneyRepository;
import com.kakaopay.spread.repository.SpreadTicketRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SearchSpreadServiceTest {

  @InjectMocks
  private SearchSpreadService searchSpreadService;

  @Mock
  private SpreadTicketRepository spreadTicketRepository;

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
    when(searchSpreadService.getSpreadTicket(SearchSpreadDto.builder().userId(1).roomId("a").token("abc").build())).thenReturn(notExpiredSpreadTicket);
  }

  @Test(expected = NotAllowedGetTicketInfoException.class)
  public void 조회_API_만료된_토큰_조회_테스트() {
    given(spreadTicketRepository.findByTokenAndRoomId("err", "b"))
      .willReturn(expiredSpreadTicket);
    when(searchSpreadService.getSpreadTicket(SearchSpreadDto.builder().userId(1).roomId("b").token("err").build()))
      .thenThrow(new NotAllowedGetTicketInfoException(NotAllowedGetTicketInfoConstant.만료된_티켓_조회_에러.getMsg()));
  }

  @Test
  public void 조회_API_토큰_생성자_호출_조회_테스트() {
    given(spreadTicketRepository.findByTokenAndRoomId("def", "a"))
      .willReturn(user1CreatedSpreadTicket);
    when(searchSpreadService.getSpreadTicket(SearchSpreadDto.builder().userId(1).roomId("a").token("def").build()))
      .thenReturn(user1CreatedSpreadTicket);
  }

  @Test(expected = NotAllowedGetTicketInfoException.class)
  public void 조회_API_토큰_비생성자_호출_조회_테스트() {
    given(spreadTicketRepository.findByTokenAndRoomId("def", "a"))
      .willReturn(user1CreatedSpreadTicket);
    when(searchSpreadService.getSpreadTicket(SearchSpreadDto.builder().userId(2).roomId("a").token("def").build()))
      .thenThrow(new NotAllowedGetTicketInfoException(NotAllowedGetTicketInfoConstant.타인_뿌리기_조회_에러.getMsg()));
  }
}