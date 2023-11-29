package com.obamax.security.service;

import com.obamax.security.config.JwtService;
import com.obamax.security.exception.BadRequestException;
import com.obamax.security.exception.ResourceNotFoundException;
import com.obamax.security.model.Role;
import com.obamax.security.model.User;
import com.obamax.security.pojo.AuthenticationRequest;
import com.obamax.security.pojo.AuthenticationResponse;
import com.obamax.security.pojo.RegisterRequest;
import com.obamax.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists!");
        }

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
