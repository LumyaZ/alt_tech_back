package com.shop.producttrialmaster.exception;

// Levée quand /account reçoit un email déjà utilisé par un autre compte
public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException(String email) {
        super("Cet email est déjà utilisé : " + email);
    }
}
