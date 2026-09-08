package com.shop.producttrialmaster.service;

import com.shop.producttrialmaster.dto.AccountRequest;
import com.shop.producttrialmaster.dto.TokenRequest;

public interface UserService {

    /**
     * Crée un nouveau compte utilisateur, mot de passe hashé.
     * Creates a new user account, with a hashed password.
     */
    void createAccount(AccountRequest request);

    /**
     * Vérifie l'email/mot de passe et renvoie un JWT si valide.
     * Verifies email/password and returns a JWT if valid.
     */
    String authenticate(TokenRequest request);
}