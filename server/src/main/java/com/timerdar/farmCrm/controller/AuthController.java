package com.timerdar.farmCrm.controller;

import com.timerdar.farmCrm.dto.AdminCreationRequest;
import com.timerdar.farmCrm.dto.AuthRequest;
import com.timerdar.farmCrm.dto.AuthResponse;
import com.timerdar.farmCrm.dto.TokenValidationRequest;
import com.timerdar.farmCrm.model.Admin;
import com.timerdar.farmCrm.service.AdminDetailsService;
import com.timerdar.farmCrm.service.AuthService;
import com.timerdar.farmCrm.service.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AdminDetailsService adminDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Environment env;

	@Autowired
	private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
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

    @PostMapping("/token")
    public ResponseEntity<?> authByToken(@RequestBody TokenValidationRequest request) {
        return authService.authByToken(request) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostConstruct
    public void initAdmin(){
        String username = env.getProperty("admin.username");
        if (!adminDetailsService.isAdminCreated(username)){
            String password = env.getProperty("admin.password");
            String encodedPassword = passwordEncoder.encode(password);
            adminDetailsService.createAdmin(username, encodedPassword);
            log.info("Админская учетка {} создана", username);
        }else{
            log.info("Админская учетка {} уже существует", username);
        }
    }
}



