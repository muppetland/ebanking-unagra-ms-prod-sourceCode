package com.unagra.ebankingapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.unagra.ebankingapi.service.OutgoingTransfersService;
import com.unagra.ebankingapi.dto.OutgoingTransfersDTO;
import com.unagra.ebankingapi.models.OutgoingTransfersResponse;

@Controller
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST })
public class OutgoingTransfersController {
	@Autowired
	private OutgoingTransfersService outgoingTransfersService;

	@PostMapping("/UNAGRA/newTransfer")
	public ResponseEntity<OutgoingTransfersResponse> newTransferUNAGRA(
			@RequestBody OutgoingTransfersDTO outgoingTransfersDTO) {
		return new ResponseEntity<>(outgoingTransfersService.newTransferUNAGRA(outgoingTransfersDTO),
				HttpStatus.CREATED);
	}
}
