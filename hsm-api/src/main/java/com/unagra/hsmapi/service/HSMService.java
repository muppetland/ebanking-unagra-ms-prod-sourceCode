package com.unagra.hsmapi.service;


import com.unagra.hsmapi.model.HSMResponse;

public interface HSMService {
	public HSMResponse matchOTP(Long customerID, String OTP, Integer moduleid);

}
