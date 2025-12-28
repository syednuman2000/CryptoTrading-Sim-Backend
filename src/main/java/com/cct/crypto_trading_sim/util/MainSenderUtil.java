package com.cct.crypto_trading_sim.util;

import com.cct.crypto_trading_sim.common.CommonHelperService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainSenderUtil {

    private final JavaMailSender mailSender;

    private static final Logger log = LoggerFactory.getLogger(MainSenderUtil.class);

    public void sendOtpMail(String toEmail, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Crypto Trading Sim - OTP Code");
            helper.setText(getOtpHtmlTemplate(otp), true);

            mimeMessage.setHeader("Content-Transfer-Encoding", "8bit");
            helper.getMimeMessage().setHeader("Content-Type", "text/html; charset=UTF-8");

            mailSender.send(mimeMessage);
        }catch (Exception e){
            log.error("❌ Failed to send OTP email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private String getOtpHtmlTemplate(String otp) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1'></head><body style='margin:0;padding:20px;background:linear-gradient(135deg,#0f0f23,#1a1a2e,#16213e);min-height:100vh;font-family:Segoe UI,Arial,sans-serif;color:#333;'>" +
                "<div style='max-width:420px;margin:0 auto;background:#fff;border-radius:20px;box-shadow:0 25px 50px rgba(0,0,0,0.25);overflow:hidden;'>" +
                "<div style='background:linear-gradient(135deg,#667eea 0%,#764ba2 50%,#f093fb 100%);padding:30px 25px;text-align:center;position:relative;overflow:hidden;'>" +
                "<h1 style='color:#fff;margin:0;font-size:28px;font-weight:700;letter-spacing:1px;position:relative;z-index:1;'>Verification Code</h1>" +
                "<p style='color:rgba(255,255,255,0.9);margin:8px 0 0;font-size:14px;font-weight:300;'>Crypto Trading Sim</p>" +
                "</div>" +
                "<div style='padding:50px 30px;text-align:center;background:linear-gradient(145deg,#f8fafc,#e8f0fe);'>" +
                "<div style='background:linear-gradient(145deg,#fff,#f0f2f5);border:3px solid transparent;background-clip:padding-box;border-radius:20px;padding:40px 20px;margin:0 auto 25px;width:280px;box-shadow:0 20px 40px rgba(102,126,234,0.15),inset 0 2px 4px rgba(255,255,255,0.6);'>" +
                "<div style='font-size:42px;font-weight:800;color:#4f46e5;font-family:Courier New,monospace;letter-spacing:12px;text-shadow:0 2px 4px rgba(0,0,0,0.1);'>" + otp + "</div>" +
                "</div>" +
                "<div style='color:#64748b;font-size:16px;line-height:1.5;'>" +
                "<p>This code will expire in <strong style='color:#4f46e5;'>5 minutes</strong></p>" +
                "</div>" +
                "</div>" +
                "<div style='padding:0 30px 40px;'>" +
                "<a href='#' style='display:block;background:linear-gradient(135deg,#4f46e5,#667eea);color:#fff;text-decoration:none;padding:16px 32px;border-radius:12px;font-weight:600;font-size:16px;text-align:center;box-shadow:0 8px 25px rgba(79,70,229,0.3);'>Continue to App</a>" +
                "</div>" +
                "<div style='background:linear-gradient(145deg,#f8fafc,#e2e8f0);padding:25px 30px;text-align:center;border-top:1px solid #e2e8f0;'>" +
                "<p style='color:#94a3b8;font-size:13px;margin:0;line-height:1.4;'>This is an automated security message from<br><strong style='color:#4f46e5;'>Crypto Trading Sim</strong></p>" +
                "</div>" +
                "</div>" +
                "</body></html>";
    }

}
