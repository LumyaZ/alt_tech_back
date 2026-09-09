package com.shop.producttrialmaster.exception;

public class EmailAlreadyUsedException extends RuntimeException {

    /*
    * Construit l'exception avec un message incluant l'email concerné
    * Builds the exception with a message including the concerned email
    */
    public EmailAlreadyUsedException(String email) {
        super("Cet email est déjà utilisé : " + email);
    }
}
