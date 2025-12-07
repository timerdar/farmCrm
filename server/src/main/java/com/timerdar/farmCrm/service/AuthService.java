package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.AuthRequest;
import com.timerdar.farmCrm.dto.AuthResponse;
import com.timerdar.farmCrm.dto.TokenValidationRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private AdminDetailsService adminDetailsService;
	@Autowired
	private JwtUtil jwtUtil;

	private final String COOKIE_NAME = "authToken";

	@Value("${jwt.lifetime}")
	private int COOKIE_MAX_AGE;

	public AuthResponse login(AuthRequest authRequest){
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authRequest.getLogin(),
				authRequest.getPassword()
		));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		final UserDetails userDetails = adminDetailsService.loadUserByUsername(authRequest.getLogin());
		final String jwt = jwtUtil.generateToken(userDetails.getUsername());

		addTokenToCookie(jwt);

		log.info("Авторизация с login = {}",authRequest.getLogin());
		return new AuthResponse(jwt);
	}

	private void addTokenToCookie(String token){
		ServletRequestAttributes attributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		if (attributes != null) {
			HttpServletResponse response = attributes.getResponse();

			Cookie cookie = new Cookie(COOKIE_NAME, token);
			cookie.setHttpOnly(true);
			cookie.setSecure(false);
			cookie.setPath("/");
			cookie.setMaxAge(COOKIE_MAX_AGE);

			response.addCookie(cookie);
		}
	}

	public boolean authByToken(TokenValidationRequest request){
		log.info("Валидация токена");
		if(jwtUtil.isTokenValid(request.getToken())){
			String username = jwtUtil.extractUsername(request.getToken());
			UserDetails userDetails = adminDetailsService.loadUserByUsername(username);

			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			return true;
		}
		return false;
	}
}
