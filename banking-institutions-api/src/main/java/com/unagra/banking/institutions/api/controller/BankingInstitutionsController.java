package com.unagra.banking.institutions.api.controller;

import com.unagra.banking.institutions.api.model.BankingInstitutionsCatalogueResponse;
import com.unagra.banking.institutions.api.model.DetailbyBankingInstitutionResponse;
import com.unagra.banking.institutions.api.services.BankingInstitutionsCatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankinginstitutions")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET })
public class BankingInstitutionsController {
	@Autowired
	private BankingInstitutionsCatalogueService bankingInstitutionsService;

	@GetMapping("/all")
	public BankingInstitutionsCatalogueResponse getBankingInstitutionsAvailableModel() {
		return bankingInstitutionsService.getBankingInstitutiosAvailableArray();
	}

	@GetMapping("/individual")
	@ResponseStatus(HttpStatus.OK)
	public DetailbyBankingInstitutionResponse getBankingInstitutionInfo(@RequestParam(required = true) String keymatch) {
		return bankingInstitutionsService.getDetailByBankInstitution(keymatch);
	}

}
