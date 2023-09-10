package com.unagra.ebankingapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.unagra.ebankingapi.dto.LoginDTO;
import com.unagra.ebankingapi.models.LoginRequest;
import com.unagra.ebankingapi.models.LoginResponse;
import com.unagra.ebankingapi.models.PasswordRecoveredRequest;
import com.unagra.ebankingapi.models.PasswordRecoveredResponse;
import com.unagra.ebankingapi.service.LoginViewService;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT })
public class LoginController {
	@Autowired
	private LoginViewService loginViewService;

	@GetMapping("/getAllCustomers")
	public List<LoginDTO> getFullData() {
		return loginViewService.getLoginData();
	}

	@GetMapping("/{customerID}")
	public ResponseEntity<LoginDTO> detailByCustomerID(@PathVariable(name = "customerID") Long customerID) {
		return ResponseEntity.ok(loginViewService.getValueByCustomerID(customerID));
	}

	@PostMapping
	public ResponseEntity<LoginResponse> matchLogin(@RequestBody LoginRequest loginRequest) {
		return new ResponseEntity<>(
				loginViewService.loginCustomer(loginRequest.getCustomerid(), loginRequest.getPassword()),
				HttpStatus.OK);
	}

	@PutMapping("/recoveryPassword/{customerID}")
	public ResponseEntity<PasswordRecoveredResponse> updatePassword(@PathVariable(name = "customerID") Long customerID,
			@RequestBody PasswordRecoveredRequest passwordRecoveryRequest) {
		PasswordRecoveredResponse dataResponse = loginViewService.recoveryPassword(customerID,
				passwordRecoveryRequest.getEmail(), passwordRecoveryRequest.getPassword(),
				passwordRecoveryRequest.getToken());
		return new ResponseEntity<>(dataResponse, HttpStatus.OK);
	}
}
