package com.shop.producttrialmaster.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("test-secret-key-at-least-32-characters-long", 60_000);
    }

    /**
     * Un token généré redonne le même email une fois décodé.
     * A generated token decodes back to the same email.
     */
    @Test
    void generateToken_thenExtractEmail_returnsSameEmail() {
        String token = jwtUtil.generateToken("user@example.com");

        assertThat(jwtUtil.extractEmail(token)).isEqualTo("user@example.com");
    }

    /**
     * Un token fraîchement généré est valide.
     * A freshly generated token is valid.
     */
    @Test
    void isTokenValid_returnsTrue_forGeneratedToken() {
        String token = jwtUtil.generateToken("user@example.com");

        assertThat(jwtUtil.isTokenValid(token)).isTrue();
    }

    /**
     * Une chaîne quelconque n'est pas un token valide.
     * A random string is not a valid token.
     */
    @Test
    void isTokenValid_returnsFalse_forGarbageToken() {
        assertThat(jwtUtil.isTokenValid("not-a-real-token")).isFalse();
    }
}
