package com.kakaopay.spread.service;

import com.kakaopay.spread.repository.DivideSpreadMoneyRepository;
import com.kakaopay.spread.repository.SpreadTicketRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateSpreadServiceTest {

  @InjectMocks
  private CreateSpreadService createSpreadService;

  @Mock
  private SpreadTicketRepository spreadTicketRepository;

  @Mock
  private DivideSpreadMoneyRepository divideSpreadMoneyRepository;

  @Before
  public void setup() {
  }
}