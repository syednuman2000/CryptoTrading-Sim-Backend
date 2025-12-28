// language: java
package com.cct.crypto_trading_sim.service;

import com.cct.crypto_trading_sim.common.CommonHelperService;
import com.cct.crypto_trading_sim.reqres.LoginRs;
import com.cct.crypto_trading_sim.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService extends CommonHelperService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public ResponseEntity<?> login(String username, String password) {
        log.debug("entering LoginService#login username={}", username);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );
        } catch (Exception e) {
            log.warn("authentication failed for username={} reason={}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(username);
        LoginRs loginRs = new LoginRs();
        loginRs.setToken(token);
        loginRs.setMessage("Logged in successfully for user: " + username);

        log.debug("exiting LoginService#login username={} tokenSet={}", username, loginRs.getToken() != null);
        return ResponseEntity.status(HttpStatus.OK).body(loginRs);
    }
}