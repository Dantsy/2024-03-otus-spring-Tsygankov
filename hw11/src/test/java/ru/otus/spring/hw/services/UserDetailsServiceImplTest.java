package ru.otus.spring.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.spring.hw.models.User;
import ru.otus.spring.hw.repositories.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Service for loading user details")
@SpringBootTest(classes = {UserDetailsServiceImpl.class})
class UserDetailsServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testUser", "password", "ROLE_USER");
    }

    @DisplayName("should return UserDetails for an existing user")
    @Test
    void loadUserByUsername_existingUser() {
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("testUser");
        assertThat(userDetails.getPassword()).isEqualTo("password");
        assertThat(userDetails.getAuthorities()).extracting("authority").containsExactly("ROLE_USER");
    }

    @DisplayName("should throw UsernameNotFoundException for non-existent user")
    @Test
    void loadUserByUsername_nonExistingUser() {
        Mockito.when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("nonExistingUser"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Could not find user with username = nonExistingUser");
    }
}
