package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.NoticesByCustomerDTO;
import com.unagra.ebankingapi.models.NoticesByCustomerResponse;

public interface NoticesByCustomerService {
	public NoticesByCustomerResponse hideNoticeByCustomer(NoticesByCustomerDTO noticesByCustomerDTO);
}
