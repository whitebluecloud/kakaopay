package com.kakaopay.spread.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpreadControllerTest {

  private MockMvc mockMvc;

  @MockBean
  SpreadController spreadController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(spreadController).build();
  }

  @Test
  public void spreadMoneyTest() {
//    given(spreadController.spreadMoney()).willReturn()
  }
}