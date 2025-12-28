package com.cct.crypto_trading_sim.service;

import com.cct.crypto_trading_sim.custom.CustomUser;
import com.cct.crypto_trading_sim.entity.UserLogin;
import com.cct.crypto_trading_sim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserLogin userLogin = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + username)
        );

        return new CustomUser(userLogin);
    }

}
