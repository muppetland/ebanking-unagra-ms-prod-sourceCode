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

import com.unagra.ebankingapi.dto.GeoCodingDTO;
import com.unagra.ebankingapi.models.GeoCodingResponse;
import com.unagra.ebankingapi.service.GeoCodingService;

@Controller
@RequestMapping("/api/geoCoding")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST })
public class GeoCodingController {
	@Autowired
	private GeoCodingService geoCodingService;

	@PostMapping("/addRecord")
	public ResponseEntity<GeoCodingResponse> addRecord(@RequestBody GeoCodingDTO geoCodingDTO) {
		// Save data...
		geoCodingService.saveValues(geoCodingDTO);

		// Generate body to confirm data created...
		final String vlMsg = "Se ha registrado la información de la geolocalización para el clienteID ["
				+ geoCodingDTO.getCustomerId().toString() + "]";

		GeoCodingResponse responseSusccesfull = new GeoCodingResponse();
		responseSusccesfull.setCustomerid(geoCodingDTO.getCustomerId());
		responseSusccesfull.setEventdatetime(geoCodingDTO.getEventDateTime());
		responseSusccesfull.setMsg(vlMsg);
		responseSusccesfull.setResponsecode(HttpStatus.CREATED.value());
		return new ResponseEntity<>(responseSusccesfull, HttpStatus.CREATED);
	}
}
