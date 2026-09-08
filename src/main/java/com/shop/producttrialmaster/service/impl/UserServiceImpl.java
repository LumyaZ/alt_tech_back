package com.shop.producttrialmaster.service.impl;

import com.shop.producttrialmaster.dto.AccountRequest;
import com.shop.producttrialmaster.dto.TokenRequest;
import com.shop.producttrialmaster.entity.User;
import com.shop.producttrialmaster.exception.EmailAlreadyUsedException;
import com.shop.producttrialmaster.repository.UserRepository;
import com.shop.producttrialmaster.security.JwtUtil;
import com.shop.producttrialmaster.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void createAccount(AccountRequest request) {
        // Vérifié ici plutôt que de laisser la contrainte unique de la base échouer :
        // une erreur SQL brute serait moins claire et plus dure à mapper au bon code HTTP
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException(request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
    }

    @Override
    public String authenticate(TokenRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}