package com.unagra.ebankingapi.models;

import com.unagra.ebankingapi.dto.NoticesViewDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetNoticesByCustomerResponse {
	private String msg;
	private String dateTimeResponse;
	private Integer noNotice;
	private List<NoticesViewDTO> noticesLit;

	public GetNoticesByCustomerResponse() {
		noticesLit = new ArrayList<>();
	}

}
