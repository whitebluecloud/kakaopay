package com.kakaopay.spread.repository;

import com.kakaopay.spread.domain.SpreadTicket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpreadTicketRepository extends MongoRepository<SpreadTicket, String> {
  SpreadTicket findByTokenAndRoomId(String token, String roomId);
}
