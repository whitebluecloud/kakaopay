package com.kakaopay.spread.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.spread.domain.Room;
import com.kakaopay.spread.domain.User;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@AutoConfigureAfter(value = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
//@ConditionalOnResource(resources = {"classpath:mongo/user.json", "classpath:mongo/room.json"})
@Slf4j
public class MongoDataPrefillConfig {

  @Value("classpath:mongo/users.json")
  private Resource userJson;

  @Value("classpath:mongo/rooms.json")
  private Resource roomJson;

  @Bean
  @SuppressWarnings("unchecked")
  public Boolean preFill(MongoTemplate mongoTemplate) throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    List<User> users = mapper.readValue(userJson.getInputStream(), new TypeReference<>() {});
    log.info("user init : {}", users);
    users.forEach(mongoTemplate::save);

    List<Room> rooms = mapper.readValue(roomJson.getInputStream(), new TypeReference<>() {});
    log.info("room init : {}", rooms);
    rooms.forEach(mongoTemplate::save);
    return true;
  }
}
