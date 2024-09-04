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
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ezp.sac.Entity.User;
import com.ezp.sac.repo.UserRepo;


class UserServiceTest {

    @InjectMocks
    private UserService userService; // Service class to be tested

    @Mock
    private UserRepo userRepo; // Mocked User repository

    @Mock
    private EncryptionBOService encryptionservice; // Mocked Encryption service

    @Mock
    private DecryptionBOService decryptionservice; // Mocked Decryption service

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Arrange: Set up test data and mock behavior
        User encryptedUser = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());
        User decryptedUser = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());

        List<User> encryptedUsers = new ArrayList<>();
        encryptedUsers.add(encryptedUser);

        when(userRepo.findAll()).thenReturn(encryptedUsers);
        when(decryptionservice.decryptUser(encryptedUser)).thenReturn(decryptedUser);

        // Act: Call the method under test
        List<User> result = userService.findAll();

        // Assert: Verify the results
        assertEquals(1, result.size(), "The result list size should be 1");
        assertEquals(decryptedUser, result.get(0), "The decrypted user should match the expected user");
        verify(userRepo).findAll(); // Verify that findAll() was called on the repository
        verify(decryptionservice).decryptUser(encryptedUser); // Verify decryption was called
    }

    @Test
    void testFindById() {
        // Arrange: Set up test data and mock behavior
        User encryptedUser = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());
        User decryptedUser = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());

        when(userRepo.findById(1L)).thenReturn(Optional.of(encryptedUser));
        when(decryptionservice.decryptUser(encryptedUser)).thenReturn(decryptedUser);

        // Act: Call the method under test
        User result = userService.findById(1L);

        // Assert: Verify the results
        assertEquals(decryptedUser, result, "The decrypted user should match the expected user");
        verify(userRepo).findById(1L); // Verify that findById() was called with the correct ID
        verify(decryptionservice).decryptUser(encryptedUser); // Verify decryption was called
    }

    @Test
    void testSaveUser() {
        // Arrange: Set up test data and mock behavior
        User user = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());
        User encryptedUser = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());

        when(encryptionservice.encryptUser(user)).thenReturn(encryptedUser);

        // Act: Call the method under test
        User result = userService.saveUser(user);

        // Assert: Verify the results
        assertEquals(encryptedUser, result, "The encrypted user should match the expected user");
        verify(encryptionservice).encryptUser(user); // Verify encryption was called
        verify(userRepo).save(encryptedUser); // Verify save was called on the repository
    }

    @Test
    void testUpdateUser() {
        // Arrange: Set up test data and mock behavior
        User existingUser = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("100.00"), false, "transPass1", new HashSet<>());
        User updatedUser = new User(1L, "user1", "pass1", "user1@example.com", new BigDecimal("200.00"), true, "transPass2", new HashSet<>());

        when(userRepo.findById(1L)).thenReturn(Optional.of(existingUser));
        when(decryptionservice.decryptUser(existingUser)).thenReturn(existingUser);
        when(encryptionservice.encryptUser(any(User.class))).thenReturn(updatedUser);
        when(userRepo.save(any(User.class))).thenReturn(updatedUser);

        // Act: Call the method under test
        User result = userService.updateUser(updatedUser);

        // Assert: Verify the results
        assertEquals(updatedUser, result, "The updated user should match the expected user");
        verify(userRepo).findById(1L); // Verify that findById() was called with the correct ID
        verify(decryptionservice).decryptUser(existingUser); // Verify decryption was called
        verify(encryptionservice).encryptUser(any(User.class)); // Verify encryption was called
        verify(userRepo).save(any(User.class)); // Verify save was called on the repository
    }
}
