package com.unagra.ebankingapi.controller;

import com.unagra.ebankingapi.dto.ThirdsAccountsDTO;
import com.unagra.ebankingapi.models.*;
import com.unagra.ebankingapi.service.ThirdsAccountsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/api/thirdsAccounts")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET,
        RequestMethod.PUT})
@Slf4j
public class ThirdsAccountsController {
    public static final String vpuCB_NTAS = "cbNTA";
    @Autowired
    private ThirdsAccountsService thirdsAccountsService;

    @GetMapping("/UNAGRA/getThirdsAccountsByCustomer")
    public ResponseEntity<ThirdsAccountsListResponse> getUNAGRATrhidsAccountsAssigned(
            @RequestParam(required = true) Long customerid,
            @RequestParam(defaultValue = "${ebanking.pageNo}", required = false) Integer pageNo,
            @RequestParam(defaultValue = "${ebanking.pageSize}", required = false) Integer pageSize) {
        return new ResponseEntity<>(thirdsAccountsService.getUNAGRAThirdsAccountsList(customerid, pageNo, pageSize),
                HttpStatus.OK);
    }

    @GetMapping("/SPEI/getThirdsAccountsByCustomer")
    public ResponseEntity<ThirdsAccountsListResponse> getSPEIThirdsAccountsAssigned(
            @RequestParam(required = true) Long customerid,
            @RequestParam(defaultValue = "${ebanking.pageNo}", required = false) Integer pageNo,
            @RequestParam(defaultValue = "${ebanking.pageSize}", required = false) Integer pageSize) {
        return new ResponseEntity<>(thirdsAccountsService.getSPEIThirdsAccountsList(customerid, pageNo, pageSize),
                HttpStatus.OK);
    }

    @PostMapping("/UNAGRA/newAccount")
    public ResponseEntity<ThirdsAccountsUNAGRAResponse> newUNAGRAThirdAccount(
            @RequestBody ThirdsAccountsDTO thirdsAccountsDTO) {
        return new ResponseEntity<>(thirdsAccountsService.newUNAGRAThirdAccount(thirdsAccountsDTO), HttpStatus.CREATED);
    }

    @PostMapping("/SPEI/newAccount")
    @CircuitBreaker(name = vpuCB_NTAS, fallbackMethod = "ratingNewThirdAccountSPEIFallback")
    public ResponseEntity<ThirdsAccountsSPEIResponse> newSPEIThirdAccount(
            @RequestBody ThirdsAccountsDTO thirdsAccountsDTO) {
        return new ResponseEntity<>(thirdsAccountsService.newSPEIThirdAccount(thirdsAccountsDTO), HttpStatus.CREATED);
    }

    @PutMapping("/UNAGRA/editAccount")
    public ResponseEntity<ThirdsAccountsUNAGRAResponse> updateUNAGRATrhidsAccount(
            @RequestBody ThirdsAccountsDTO thirdsAccountsDTO) {
        return new ResponseEntity<>(thirdsAccountsService.updateUNAGRAThridAccount(thirdsAccountsDTO), HttpStatus.OK);
    }

    @PutMapping("/SPEI/editAccount")
    public ResponseEntity<ThirdsAccountsUNAGRAResponse> updateSPEITrhidsAccount(
            @RequestBody ThirdsAccountsDTO thirdsAccountsDTO) {
        return new ResponseEntity<>(thirdsAccountsService.updateSPEIThridAccount(thirdsAccountsDTO), HttpStatus.OK);
    }

    @DeleteMapping("/UNAGRA/deleteAccount")
    public ResponseEntity<ThirdsAccountsUNAGRAResponse> deleteUNAGRAThirdAccount(
            @RequestBody ThirdsAccountsDeleteRequest thirdsAccountsDeleteRequest) {
        return new ResponseEntity<>(thirdsAccountsService.deleteUNAGRAThridAccount(thirdsAccountsDeleteRequest),
                HttpStatus.OK);
    }

    @DeleteMapping("/SPEI/deleteAccount")
    public ResponseEntity<ThirdsAccountsUNAGRAResponse> deleteSPEIThirdAccount(
            @RequestBody ThirdsAccountsDeleteRequest thirdsAccountsDeleteRequest) {
        return new ResponseEntity<>(thirdsAccountsService.deleteSPEIThridAccount(thirdsAccountsDeleteRequest),
                HttpStatus.OK);
    }

    //ThirdsAccountsSPEIResponse
    public ResponseEntity<FallBackResponse> ratingNewThirdAccountSPEIFallback(Exception exception) {
        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        String vlMsg = "Response devuelto derivado a que el servicio invocado no se encuentra disponible.";
        //log.info("Response devuelto derivado a que el servicio invocado no se encuentra disponible.", exception.getMessage());
        log.info("Response devuelto derivado a que el servicio invocado no se encuentra disponible.");


        FallBackResponse fallBackResponse = new FallBackResponse()
                .builder()
                .dateTime(dateFormat.format(date))
                .responseCode(HttpStatus.NO_CONTENT.value())
                .exception(exception.getMessage())
                .msg(vlMsg)
                .build();
        return new ResponseEntity<>(fallBackResponse, HttpStatus.CONFLICT);
    }
}
