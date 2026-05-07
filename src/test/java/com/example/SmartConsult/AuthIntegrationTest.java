package com.example.SmartConsult;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUserRegistrationEndpoint() throws Exception {
        String registerJson = "{"
                + "\"name\": \"Test User\","
                + "\"email\": \"test@example.com\","
                + "\"password\": \"TestPass123\","
                + "\"role\": \"PATIENT\""
                + "}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testLoginEndpoint() throws Exception {
        // First register a user
        String registerJson = "{"
                + "\"name\": \"Test User\","
                + "\"email\": \"logintest@example.com\","
                + "\"password\": \"TestPass123\","
                + "\"role\": \"PATIENT\""
                + "}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson));

        // Then attempt to login
        String loginJson = "{"
                + "\"email\": \"logintest@example.com\","
                + "\"password\": \"TestPass123\""
                + "}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void testInvalidCredentialsLogin() throws Exception {
        String loginJson = "{"
                + "\"email\": \"nonexistent@example.com\","
                + "\"password\": \"wrongpassword\""
                + "}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testValidationOnEmptyFields() throws Exception {
        String invalidJson = "{"
                + "\"name\": \"\","
                + "\"email\": \"\","
                + "\"password\": \"\","
                + "\"role\": \"PATIENT\""
                + "}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
