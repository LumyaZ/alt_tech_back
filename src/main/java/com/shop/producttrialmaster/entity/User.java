package com.shop.producttrialmaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String firstname;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    // Stocké hashé (BCrypt), @JsonIgnore : ne doit jamais sortir dans une réponse JSON,
    // même si User est un jour sérialisé directement (ex. via une relation depuis Cart)
    @NotBlank
    @Column(nullable = false)
    @JsonIgnore
    private String password;
}