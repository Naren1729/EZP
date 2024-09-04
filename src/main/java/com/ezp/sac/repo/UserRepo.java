/**
 * UserRepo is a repository interface for managing {@link User} entities.
 * 
 * It extends JpaRepository to provide standard CRUD operations and query methods for handling
 * transaction details.
 * 
 * The repository is annotated with @Repository to indicate it is a Spring Data repository.
 */


package com.ezp.sac.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezp.sac.Entity.User;


@Repository
public interface UserRepo extends JpaRepository<User,Long > {
	
	public Optional<User> findByUsername(String username);

}
