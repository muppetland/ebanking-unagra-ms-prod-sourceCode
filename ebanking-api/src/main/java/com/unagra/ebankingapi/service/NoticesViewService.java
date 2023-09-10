package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.models.GetNoticesByCustomerResponse;

public interface NoticesViewService {
	public GetNoticesByCustomerResponse getAllNoticesByCustomer(Long customerID);
}
