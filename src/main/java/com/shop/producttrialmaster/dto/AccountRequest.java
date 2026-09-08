package com.shop.producttrialmaster.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String firstname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}