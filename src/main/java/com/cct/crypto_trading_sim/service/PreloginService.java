package com.cct.crypto_trading_sim.service;

import com.cct.crypto_trading_sim.common.CommonHelperService;
import com.cct.crypto_trading_sim.entity.UserLogin;
import com.cct.crypto_trading_sim.entity.UserProfile;
import com.cct.crypto_trading_sim.enums.UserRoleEnum;
import com.cct.crypto_trading_sim.exception.UsernameAlreadyExistException;
import com.cct.crypto_trading_sim.repository.UserProfileRepository;
import com.cct.crypto_trading_sim.repository.UserRepository;
import com.cct.crypto_trading_sim.reqres.RegisterRq;
import com.cct.crypto_trading_sim.reqres.CreateRs;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreloginService extends CommonHelperService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileRepository profileRepository;

    private static final Logger log = LoggerFactory.getLogger(PreloginService.class);

    public CreateRs register(RegisterRq registerRq) {
        log.debug("entering PreloginService#register username={}", registerRq.getUsername());

        if (userRepository.findByUsername(registerRq.getUsername()).isPresent()) {
            log.warn("register attempt with existing username={}", registerRq.getUsername());
            throw new UsernameAlreadyExistException(registerRq.getUsername()+ " username already exists");
        }

        UserLogin user = new UserLogin();
        user.setUsername(registerRq.getUsername());
        user.setPassword(passwordEncoder.encode(registerRq.getPassword()));
        user.setRole(UserRoleEnum.ROLE_USER.name());
        userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setUserLogin(user);
        profile.setFullName(registerRq.getFullName());
        profile.setEmail(registerRq.getEmail());
        profile.setPhoneNumber(registerRq.getPhoneNo());
        profileRepository.save(profile);

        log.debug("exiting PreloginService#register username={} -> success", registerRq.getUsername());
        return new CreateRs("User registered successfully");
    }
}
