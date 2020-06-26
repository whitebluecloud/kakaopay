package com.kakaopay.spread.repository;

import com.kakaopay.spread.domain.DivideSpreadMoney;
import com.kakaopay.spread.domain.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivideSpreadMoneyRepository extends MongoRepository<DivideSpreadMoney, String> {
  List<DivideSpreadMoney> findAllByToken(String token);
}
