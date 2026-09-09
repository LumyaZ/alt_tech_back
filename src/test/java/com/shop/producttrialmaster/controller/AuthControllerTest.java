package com.shop.producttrialmaster.controller;

import com.shop.producttrialmaster.entity.User;
import com.shop.producttrialmaster.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void createUser(String email, String rawPassword) {
        User user = new User();
        user.setUsername("jdoe");
        user.setFirstname("John");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }

    /**
     * Connexion réussie renvoie un token.
     * Successful login returns a token.
     */
    @Test
    void login_returns200_andToken_whenCredentialsAreValid() throws Exception {
        createUser("jdoe@example.com", "secret123");

        mockMvc.perform(post("/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "jdoe@example.com",
                                "password", "secret123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    /**
     * Mauvais mot de passe rejeté.
     * Wrong password rejected.
     */
    @Test
    void login_returns401_whenPasswordIsWrong() throws Exception {
        createUser("jdoe@example.com", "secret123");

        mockMvc.perform(post("/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "jdoe@example.com",
                                "password", "wrong-password"))))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Email inconnu rejeté.
     * Unknown email rejected.
     */
    @Test
    void login_returns401_whenEmailIsUnknown() throws Exception {
        mockMvc.perform(post("/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "unknown@example.com",
                                "password", "secret123"))))
                .andExpect(status().isUnauthorized());
    }
}
