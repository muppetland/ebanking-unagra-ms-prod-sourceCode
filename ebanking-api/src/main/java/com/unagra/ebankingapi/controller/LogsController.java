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

import com.unagra.ebankingapi.dto.LogsDTO;
import com.unagra.ebankingapi.models.LogsResponse;
import com.unagra.ebankingapi.service.LogsService;

@Controller
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST })
public class LogsController {
	@Autowired
	private LogsService logsService;

	@PostMapping("/addRecord")
	public ResponseEntity<LogsResponse> addRecord(@RequestBody LogsDTO logsDTO) {
		// Save data...
		logsService.saveValues(logsDTO);

		// Generate body to confirm data created...
		LogsResponse responseSusccesfull = new LogsResponse();
		responseSusccesfull.setAction(logsDTO.getAction());
		responseSusccesfull.setCustomerid(logsDTO.getCustomerid());
		responseSusccesfull.setEnviorment(logsDTO.getEnviormentid());
		responseSusccesfull.setEventdatetime(logsDTO.getEventdatetime().toString());
		responseSusccesfull.setModule(logsDTO.getModuleid());
		responseSusccesfull.setMsg("Se ha registrado exitosamente el log de actividad para el clienteID ["
				+ logsDTO.getCustomerid().toString() + "]");
		responseSusccesfull.setResponsecode(HttpStatus.CREATED.value());
		return new ResponseEntity<>(responseSusccesfull, HttpStatus.CREATED);
	}
}