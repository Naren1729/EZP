/**
 * @author: Bhavansh
 * @date: 3/09/2024
 * 
 * @description:
 * Unit tests for the UserService class.
 * This test class uses Mockito to mock dependencies and verify the behavior of the UserService methods:
 * - findAll() retrieves and decrypts all users.
 * - findById() retrieves and decrypts a user by ID.
 * - saveUser() encrypts and saves a new user.
 * - updateUser() decrypts the existing user, encrypts the updated user, and saves it.
 */

package com.ezp.sac.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ezpay.entity.User;
import com.ezpay.repository.UserRepo;
import com.ezpay.service.UserService;


class UserServiceTest {

    @InjectMocks
    private UserService userService; // Service class to be tested

    @Mock
    private UserRepo userRepo; // Mocked User repository

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Arrange: Set up test data and mock behavior
        User user = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepo.findAll()).thenReturn(users);

        // Act: Call the method under test
        List<User> result = userService.findAll();

        // Assert: Verify the results
        assertEquals(1, result.size(), "The result list size should be 1");
        assertEquals(user, result.get(0), "The user should match the expected user");
        verify(userRepo).findAll(); // Verify that findAll() was called on the repository
    }

    @Test
    void testFindById() {
        // Arrange: Set up test data and mock behavior
        User user = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        // Act: Call the method under test
        User result = userService.findById(1L);

        // Assert: Verify the results
        assertEquals(user, result, "The user should match the expected user");
        verify(userRepo).findById(1L); // Verify that findById() was called with the correct ID
    }

    @Test
    void testSaveUser() {
        // Arrange: Set up test data and mock behavior
        User user = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());

        when(userRepo.save(user)).thenReturn(user);

        // Act: Call the method under test
        User result = userService.saveUser(user);

        // Assert: Verify the results
        assertEquals(user, result, "The saved user should match the expected user");
        verify(userRepo).save(user); // Verify save was called on the repository
    }
    
    @Test
    void testFindByUsername() {
        // Arrange: Set up test data and mock behavior
        String username = "user1";
        User user = new User(1L, username, "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));

        // Act: Call the method under test
        User result = userService.findByUsername(username);

        // Assert: Verify the results
        assertNotNull(result, "The result should not be null");
        assertEquals(user, result, "The user should match the expected user");

        verify(userRepo).findByUsername(username); // Verify repository lookup was called
    }

    
    @Test
    void testUpdateUser() {
        // Arrange: Set up test data and mock behavior
        User existingUser = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());
        User updatedUser = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("200.00"), true, "transPass2", new HashSet<>());

        when(userRepo.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepo.save(updatedUser)).thenReturn(updatedUser);

        // Act: Call the method under test
        User result = userService.updateUser(updatedUser.getId(),updatedUser);

        // Assert: Verify the results
        assertEquals(updatedUser, result, "The updated user should match the expected user");
        verify(userRepo).findById(1L); // Verify that findById() was called with the correct ID
        verify(userRepo).save(updatedUser); // Verify save was called on the repository
    }
}
