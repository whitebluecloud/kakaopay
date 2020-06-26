package com.kakaopay.spread.repository;

import com.kakaopay.spread.domain.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {

}
