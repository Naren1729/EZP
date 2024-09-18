/**
 * @author: Naren
 * @date: 30/08/2024
 * 
 * @description:
 * FraudRepo is a repository interface for managing {@link FraudTransactionDetails} entities.
 * 
 * It extends JpaRepository to provide CRUD operations and custom query methods for handling
 * fraudulent transaction details.
 * 
 * The repository is annotated with @Repository to indicate it's a Spring Data repository.
 */


package com.ezpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezpay.entity.FraudTransactionDetails;

@Repository
public interface FraudRepo extends JpaRepository<FraudTransactionDetails, Long> {

}
