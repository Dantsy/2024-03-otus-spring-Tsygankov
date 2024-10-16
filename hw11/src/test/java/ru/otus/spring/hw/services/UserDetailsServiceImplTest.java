package ru.otus.spring.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.spring.hw.models.User;
import ru.otus.spring.hw.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test loadUserByUsername with existing user")
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole("ROLE_USER");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Test loadUserByUsername with existing admin")
    void loadUserByUsername_AdminExists_ReturnsUserDetails() {
        String username = "adminUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("adminPassword");
        user.setRole("ROLE_ADMIN");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Test loadUserByUsername with non-existent user")
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
    }

    @Test
    @DisplayName("Test loadUserByUsername with user having no role")
    void loadUserByUsername_UserHasNoRole_ThrowsIllegalArgumentException() {
        String username = "userWithNoRole";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole(null); // No role set

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
    }

    @Test
    @DisplayName("Test loadUserByUsername with user having no password")
    void loadUserByUsername_UserHasNoPassword_ThrowsIllegalArgumentException() {
        String username = "userWithNoPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(null); // No password set
        user.setRole("ROLE_USER");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
    }

    @Test
    @DisplayName("Test loadUserByUsername with user having no username")
    void loadUserByUsername_UserHasNoUsername_ThrowsIllegalArgumentException() {
        String username = null;
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole("ROLE_USER");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
    }
}