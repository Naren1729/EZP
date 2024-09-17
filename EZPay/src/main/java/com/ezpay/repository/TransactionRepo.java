package com.ezpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ezpay.entity.TransactionDetails;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionDetails, Long> {

    List<TransactionDetails> findByUsernIdAndDestinationUserIdAndTransactionTimeBetween(Long usernId, Long destinationUserId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}