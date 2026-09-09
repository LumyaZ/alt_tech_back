package com.shop.producttrialmaster.controller;

import com.shop.producttrialmaster.dto.TokenRequest;
import com.shop.producttrialmaster.dto.TokenResponse;
import com.shop.producttrialmaster.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * Vérifie l'email/mot de passe et renvoie un token JWT si valide.
     * Verifies email/password and returns a JWT token if valid.
     */
    @PostMapping("/token")
    public TokenResponse login(@Valid @RequestBody TokenRequest request) {
        String token = userService.authenticate(request);
        return new TokenResponse(token);
    }
}