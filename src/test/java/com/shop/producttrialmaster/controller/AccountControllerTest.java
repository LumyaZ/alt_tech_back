package com.shop.producttrialmaster.controller;

import com.shop.producttrialmaster.entity.User;
import com.shop.producttrialmaster.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createAccount_returns201_andStoresHashedPassword() throws Exception {
        Map<String, String> payload = Map.of(
                "username", "jdoe",
                "firstname", "John",
                "email", "jdoe@example.com",
                "password", "secret123"
        );

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());

        Optional<User> saved = userRepository.findByEmail("jdoe@example.com");
        assertThat(saved).isPresent();
        assertThat(saved.get().getPassword()).isNotEqualTo("secret123");
    }

    @Test
    void createAccount_returns400_whenEmailIsInvalid() throws Exception {
        Map<String, String> payload = Map.of(
                "username", "jdoe",
                "firstname", "John",
                "email", "not-an-email",
                "password", "secret123"
        );

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }
}
