package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.models.HSMResponse;

public interface HSMService {
	public HSMResponse matchOTP(Long customerID, String OTP, Integer moduleid);

}
