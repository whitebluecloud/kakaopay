package com.kakaopay.spread.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.spread.dto.spread.SpreadCreateReqDto;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpreadControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Before
  public void setup() {
  }

  @Test
  public void 뿌리기_API_분배_검증_테스트() throws Exception {
    mockMvc.perform(post("/spread")
      .characterEncoding("utf-8")
      .header("X-USER-ID", 1)
      .header("X-ROOM-ID", "a")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(SpreadCreateReqDto.builder().amount(1000).headCount(1).build())))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(jsonPath("token").value(Matchers.hasLength(3)))
      .andDo(print());
  }

  @Test
  public void 뿌리기_API_토큰_3자리_테스트() throws Exception {
    mockMvc.perform(post("/spread")
      .characterEncoding("utf-8")
      .header("X-USER-ID", 1)
      .header("X-ROOM-ID", "a")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(SpreadCreateReqDto.builder().amount(1000).headCount(1).build())))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(jsonPath("token").value(Matchers.hasLength(3)))
      .andDo(print());
  }
}
