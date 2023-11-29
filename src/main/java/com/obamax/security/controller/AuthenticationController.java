package com.obamax.security.controller;

import com.obamax.security.pojo.AuthenticationRequest;
import com.obamax.security.pojo.AuthenticationResponse;
import com.obamax.security.pojo.RegisterRequest;
import com.obamax.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(service.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }

    @GetMapping("/logout/success")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout success!");
    }
}
