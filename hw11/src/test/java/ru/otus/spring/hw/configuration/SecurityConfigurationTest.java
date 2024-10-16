package ru.otus.spring.hw.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("Security Configuration Test")
public class SecurityConfigurationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Should allow access to public endpoints for authenticated users")
    @WithMockUser(username = "user", roles = {"USER"})
    public void shouldAllowAccessToPublicEndpointsForAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should deny access to admin endpoints for non-admin users")
    @WithMockUser(username = "user", roles = {"USER"})
    public void shouldDenyAccessToAdminEndpointsForNonAdminUsers() throws Exception {
        mockMvc.perform(get("/books/edit"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/books/delete"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should allow access to admin endpoints for admin users")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldAllowAccessToAdminEndpointsForAdminUsers() throws Exception {
        mockMvc.perform(get("/books/edit")
                        .param("id", "1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/books/delete")
                        .param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should deny access to any other endpoints")
    @WithMockUser(username = "user", roles = {"USER"})
    public void shouldDenyAccessToAnyOtherEndpoints() throws Exception {
        mockMvc.perform(get("/unknown"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should redirect to login for unauthenticated users")
    public void shouldRedirectToLoginForUnauthenticatedUsers() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/authors"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/books"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/genres"))
                .andExpect(status().is3xxRedirection());
    }

}