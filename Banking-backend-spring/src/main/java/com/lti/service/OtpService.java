package com.lti.service;

import java.time.LocalDateTime;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.lti.entity.AccountDetail;
import com.lti.entity.Otp;

@Service
public class OtpService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	CustomerService service = new CustomerService();
	
	public void sendOtpMessage(String email, AccountDetail accDetail) throws MessagingException {
	
		String NumericString = "1234567890";
		StringBuilder sb = new StringBuilder(6);
		for(int i=0; i<6; i++){
			int index = (int) (NumericString.length()
					*Math.random());
			sb.append(NumericString.charAt(index));
		}
		String OTP = sb.toString();
		Otp otp = new Otp();
		otp.setOtp(OTP);
		otp.setDateTime(LocalDateTime.now());
		otp.setFromAccount(accDetail);
		 MimeMessage msg = javaMailSender.createMimeMessage();

		 String message = "Your One Time Password is" + OTP +" \nPlease don't share your OTP with anyone.";
		 
	        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

	        helper.setFrom("serviceslti@hotmail.com");
	        helper.setTo(email);
	        helper.setSubject("OTP Confirmation");
	        helper.setText(message, true);
	        javaMailSender.send(msg);
   }
	
	

}
