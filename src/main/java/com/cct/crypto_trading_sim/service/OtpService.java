package com.cct.crypto_trading_sim.service;

import com.cct.crypto_trading_sim.entity.UserLogin;
import com.cct.crypto_trading_sim.entity.UserOtp;
import com.cct.crypto_trading_sim.entity.UserProfile;
import com.cct.crypto_trading_sim.enums.RecordStatusEnum;
import com.cct.crypto_trading_sim.exception.OtpValidationFailedException;
import com.cct.crypto_trading_sim.repository.UserOtpRepository;
import com.cct.crypto_trading_sim.repository.UserProfileRepository;
import com.cct.crypto_trading_sim.repository.UserRepository;
import com.cct.crypto_trading_sim.reqres.CreateRq;
import com.cct.crypto_trading_sim.reqres.CreateRs;
import com.cct.crypto_trading_sim.util.MainSenderUtil;
import com.cct.crypto_trading_sim.util.OtpGeneratorUtil;
import com.cct.crypto_trading_sim.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final UserRepository userRepository;
    private final UserOtpRepository userOtpRepository;
    private final UserProfileRepository userProfileRepository;
    private final MainSenderUtil mainSenderUtil;

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);

    public CreateRs generateOtp(CreateRq createRq) {
        log.debug("entering OtpService#generateOtp");
        expireOtps(createRq.getCode());
        String otp = OtpGeneratorUtil.generateOtp();
        UserProfile userProfile = userProfileRepository.findByUserLoginId(SecurityUtil.getCurrentUserId()).orElse(null);
        mainSenderUtil.sendOtpMail(userProfile!=null ? userProfile.getEmail() : "", otp);
        addUserOtpRecord(otp, createRq.getCode());
        log.debug("exiting OtpService#generateOtp");
        return new CreateRs("OTP generated successfully");
    }

    public CreateRs validateOtp(CreateRq createRq) {
        log.debug("entering OtpService#validateOtp");
        UserOtp userOtp = userOtpRepository.getLatestPendingOtp(SecurityUtil.getCurrentUserId(), createRq.getCode());
        if(userOtp==null || !userOtp.getOtp().equalsIgnoreCase(createRq.getId())){
            throw new OtpValidationFailedException("Otp Validation Failed!");
        }
        userOtp.setStatus(RecordStatusEnum.VALIDATED.name());
        userOtpRepository.save(userOtp);
        log.debug("exiting OtpService#validateOtp");
        return new CreateRs("OTP validated successfully");
    }

    private void addUserOtpRecord(String otp, String serviceCode){
        log.debug("adding record in UserOtp table");
        UserLogin userLogin = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElse(null);
        UserOtp userOtp = new UserOtp();
        userOtp.setUserLogin(userLogin);
        userOtp.setOtp(otp);
        userOtp.setStatus(RecordStatusEnum.PENDING.name());
        userOtp.setServiceCode(serviceCode);
        userOtp.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        userOtpRepository.save(userOtp);
        log.debug("exiting addUserOtpRecord");
    }

    private void expireOtps(String serviceCode){
        log.debug("expiring old OTPs");
        List<UserOtp> userOtps = userOtpRepository.getAllExpiredOtps();
        for(UserOtp userOtp : userOtps){
            userOtp.setStatus(RecordStatusEnum.EXPIRED.name());
            userOtpRepository.save(userOtp);
        }
        log.debug("expiring same serviceCode OTPs");
        UserOtp userOtp = userOtpRepository.getSameServiceCodeOtp(SecurityUtil.getCurrentUserId(), serviceCode);
        if(userOtp!=null) {
            userOtp.setStatus(RecordStatusEnum.EXPIRED.name());
            userOtpRepository.save(userOtp);
        }
        log.debug("exiting expireOldOtps");
    }

}
