package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.models.AccountExtraInfoResponse;

public interface AccountExtraInfoService {
	public AccountExtraInfoResponse getDetailByAccount(Long accountid);
}
