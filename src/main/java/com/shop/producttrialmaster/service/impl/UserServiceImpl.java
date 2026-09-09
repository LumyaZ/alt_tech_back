package com.shop.producttrialmaster.service.impl;

import com.shop.producttrialmaster.dto.AccountRequest;
import com.shop.producttrialmaster.dto.TokenRequest;
import com.shop.producttrialmaster.entity.User;
import com.shop.producttrialmaster.exception.EmailAlreadyUsedException;
import com.shop.producttrialmaster.repository.UserRepository;
import com.shop.producttrialmaster.security.JwtUtil;
import com.shop.producttrialmaster.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

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
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException(request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstname(request.getFirstname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    public String authenticate(TokenRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        return jwtUtil.generateToken(user.get().getEmail());
    }
}