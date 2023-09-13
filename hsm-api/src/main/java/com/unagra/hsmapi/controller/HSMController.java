package com.unagra.hsmapi.controller;

import com.unagra.hsmapi.model.HSMRequest;
import com.unagra.hsmapi.model.HSMResponse;
import com.unagra.hsmapi.service.HSMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hsm")
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