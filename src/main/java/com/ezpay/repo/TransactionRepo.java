/**
 * @author: Naren
 * @date: 30/08/2024
 * 
 * @description:
 * TransactionRepo is a repository interface for managing {@link TransactionDetails} entities.
 * 
 * It extends JpaRepository to provide standard CRUD operations and query methods for handling
 * transaction details.
 * 
 * The repository is annotated with @Repository to indicate it is a Spring Data repository.
 */


package com.ezpay.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezpay.entity.TransactionDetails;


@Repository
public interface TransactionRepo extends JpaRepository<TransactionDetails,Long> {

}
