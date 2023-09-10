package com.unagra.ebankingapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.unagra.ebankingapi.dto.NoticesByCustomerDTO;
import com.unagra.ebankingapi.models.GetNoticesByCustomerResponse;
import com.unagra.ebankingapi.models.NoticesByCustomerResponse;
import com.unagra.ebankingapi.service.NoticesByCustomerService;
import com.unagra.ebankingapi.service.NoticesViewService;

@RestController
@RequestMapping("/api/notices")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.PUT })
public class NoticesController {
	@Autowired
	private NoticesViewService noticesViewService;

	@Autowired
	private NoticesByCustomerService noticesByCustomerService;

	@GetMapping("/{customerid}")
	public ResponseEntity<GetNoticesByCustomerResponse> getAllNoticesByCustomer(
			@PathVariable("customerid") Long customerid) {
		return new ResponseEntity<>(noticesViewService.getAllNoticesByCustomer(customerid), HttpStatus.OK);
	}

	@PutMapping("/hide")
	public ResponseEntity<NoticesByCustomerResponse> hideNoticesByCustomer(
			@RequestBody NoticesByCustomerDTO noticesByCustomerDTO) {
		return new ResponseEntity<>(noticesByCustomerService.hideNoticeByCustomer(noticesByCustomerDTO), HttpStatus.OK);
	}
}
