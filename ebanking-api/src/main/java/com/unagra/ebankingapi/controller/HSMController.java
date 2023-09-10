package com.unagra.ebankingapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.unagra.ebankingapi.models.HSMRequest;
import com.unagra.ebankingapi.models.HSMResponse;
import com.unagra.ebankingapi.service.HSMService;

@RestController
@RequestMapping("/api/hsm")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST })
public class HSMController {
	@Autowired
	private HSMService hsmServie;

	@PostMapping
	public ResponseEntity<HSMResponse> matchLogin(@RequestBody HSMRequest hsmRequest) {
		// validate otp send...
		return new ResponseEntity<>(hsmServie.matchOTP(hsmRequest.getCustomerid(), hsmRequest.getOtp(), hsmRequest.getModuleid()), HttpStatus.OK);
	}
}