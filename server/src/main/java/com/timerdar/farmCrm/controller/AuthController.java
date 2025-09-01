package com.timerdar.farmCrm.controller;

import com.timerdar.farmCrm.dto.AdminCreationRequest;
import com.timerdar.farmCrm.dto.AuthRequest;
import com.timerdar.farmCrm.dto.AuthResponse;
import com.timerdar.farmCrm.dto.TokenValidationRequest;
import com.timerdar.farmCrm.model.Admin;
import com.timerdar.farmCrm.service.AdminDetailsService;
import com.timerdar.farmCrm.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AdminDetailsService adminDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getLogin(),
                authRequest.getPassword()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильный логин или пароль");
        }
        final UserDetails userDetails = adminDetailsService.loadUserByUsername(authRequest.getLogin());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/check-login")
    public ResponseEntity<?> checkLogin(@RequestBody TokenValidationRequest request) {
        return ResponseEntity.ok(jwtUtil.validateToken(request.getToken(), request.getLogin()));
    }

    @PostMapping("/registrate-admin")
    public ResponseEntity<?> createAdmin(@RequestBody AdminCreationRequest admin) {
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        Admin created = adminDetailsService.createAdmin(admin.getLogin(), encodedPassword);
        return ResponseEntity.ok(created);
    }
}



