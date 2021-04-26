package com.lti.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class OtpService {
	
	public String generateOtp() {
		String NumericString = "1234567890";
		StringBuilder sb = new StringBuilder(6);
		for(int i=0; i<6; i++){
			int index = (int) (NumericString.length()
					*Math.random());
			sb.append(NumericString.charAt(index));
		}
		String OTP = sb.toString();
		return OTP;
	}

	
	
	

}
