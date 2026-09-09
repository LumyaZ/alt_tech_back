package com.shop.producttrialmaster.controller;

import com.shop.producttrialmaster.dto.AccountRequest;
import com.shop.producttrialmaster.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

    /**
     * Crée un nouveau compte utilisateur, mot de passe hashé.
     * Creates a new user account, with a hashed password.
     */
    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@Valid @RequestBody AccountRequest request) {
        userService.createAccount(request);
    }
}