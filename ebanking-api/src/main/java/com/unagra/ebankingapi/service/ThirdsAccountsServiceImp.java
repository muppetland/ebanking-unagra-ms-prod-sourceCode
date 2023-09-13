package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.DetailBankInstitutionDTO;
import com.unagra.ebankingapi.dto.ThirdsAccountsDTO;
import com.unagra.ebankingapi.entities.ebanking.ThirdsAccounts;
import com.unagra.ebankingapi.exceptions.ResourceNotFoundException;
import com.unagra.ebankingapi.models.ThirdsAccountsDeleteRequest;
import com.unagra.ebankingapi.models.ThirdsAccountsListResponse;
import com.unagra.ebankingapi.models.ThirdsAccountsSPEIResponse;
import com.unagra.ebankingapi.models.ThirdsAccountsUNAGRAResponse;
import com.unagra.ebankingapi.repository.ebanking.BankingInstitutionsRepository;
import com.unagra.ebankingapi.repository.ebanking.OtpTokenRepository;
import com.unagra.ebankingapi.repository.ebanking.ThirdsAccountsRepository;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ThirdsAccountsServiceImp implements ThirdsAccountsService {
    private static final String vgSourceUNAGRA = "UNAGRA";
    private static final String vgSourceSPEI = "INTERBANCARIAS";
    @Value("${ebanking.unagraID}")
    public String vpuUNAGRAID;
    private Logger logger = LoggerFactory.getLogger(ThirdsAccountsService.class);
    @Autowired
    private ThirdsAccountsRepository thirdsAccountsRepository;
    @Autowired
    private BankingInstitutionsRepository bankingInstitutionsRepository;

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ThirdsAccountsUNAGRAResponse newUNAGRAThirdAccount(ThirdsAccountsDTO thirdsAccountsDTO) {
        // if token sent doesn't valid, we can't continues...
        String vlValidateToken = otpTokenRepository.findTokenByCustomer(thirdsAccountsDTO.getCustomerid(),
                thirdsAccountsDTO.getToken());
        if (vlValidateToken == null) {
            throw new ResourceNotFoundException(
                    "El token proporcionado no tiene coincidencia con el proceso realizado por el clienteID ["
                            + thirdsAccountsDTO.getCustomerid().toString()
                            + "], favor de proporcionar el token correcto para esta solicitud.");
        }

        // According data sent we need to add extra information to DTO...
        thirdsAccountsDTO.setStatusid("v");
        thirdsAccountsDTO.setIsunagra(1);
        switch (thirdsAccountsDTO.getAccountcard().trim().length()) {
            case 18: {
                thirdsAccountsDTO.setIstce("INTERBANK_ACCOUNT");
                break;
            }
            case 16: {
                thirdsAccountsDTO.setIstce("CARD");
                break;
            }
            default: {
                thirdsAccountsDTO.setIstce("INTERNAL_ACCOUNT");
                break;
            }
        }

        // before to add this account we need to validate that doesn't exists...
        ThirdsAccounts vlExistsAccount = thirdsAccountsRepository.AccountExists(thirdsAccountsDTO.getCustomerid(),
                thirdsAccountsDTO.getAccountcard(), 1);
        if (vlExistsAccount != null) {
            throw new BadRequestException("¡Ups!, lo sentimos la cuentaID del beneficiario [" + thirdsAccountsDTO.getAccountcard() + "] ya ha sido registrada previamente.");
        }

        // convert DTO to Entity...
        ThirdsAccounts thirdsAccounts = mapToEntity(thirdsAccountsDTO);

        // Save Data...
        ThirdsAccounts newRecord = thirdsAccountsRepository.save(thirdsAccounts);

        // close token sent to prevent use it in others services...
        otpTokenRepository.updateStatusTokenByCustomer(thirdsAccountsDTO.getCustomerid(), thirdsAccountsDTO.getToken());

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // return a model with data...
        ThirdsAccountsUNAGRAResponse thirdsAccountsResponse = new ThirdsAccountsUNAGRAResponse()
                .builder()
                .origin(vgSourceUNAGRA)
                .dateTimeResponse(dateFormat.format(date))
                .thirdID(newRecord.getThirdid())
                .msg("Se ha asociado de manera exitosa la cuenta de tercero "
                        + thirdsAccountsDTO.getAccountcard()
                        + " al clienteID " + thirdsAccountsDTO.getCustomerid())
                .build();

        // return dto to json...
        return thirdsAccountsResponse;
    }

    @Override
    public ThirdsAccountsUNAGRAResponse deleteUNAGRAThridAccount(ThirdsAccountsDeleteRequest thirdsAccountsDeleteRequest) {
        // if token sent doesn't valid, we can't continues...
        String vlValidateToken = otpTokenRepository.findTokenByCustomer(thirdsAccountsDeleteRequest.getCustomerid(),
                thirdsAccountsDeleteRequest.getToken());
        if (vlValidateToken == null) {
            throw new ResourceNotFoundException(
                    "El token proporcionado no tiene coincidencia con el proceso realizado por el clienteID ["
                            + thirdsAccountsDeleteRequest.getCustomerid().toString()
                            + "], favor de proporcionar el token correcto para esta solicitud.");
        }

        // we need to find this id in our table to process...
        Integer vlResult = thirdsAccountsRepository.findThirdAccountById(thirdsAccountsDeleteRequest.getThirdid(), 1);
        if (vlResult == 0) {
            throw new ResourceNotFoundException("La cuenta de terceros | " + vgSourceUNAGRA + " con ID ["
                    + thirdsAccountsDeleteRequest.getThirdid() + "] no se encuentra.");
        }

        // delete record sent...
        thirdsAccountsRepository.deleteById(thirdsAccountsDeleteRequest.getThirdid());

        // close token sent to prevent use it in others services...
        otpTokenRepository.updateStatusTokenByCustomer(thirdsAccountsDeleteRequest.getCustomerid(),
                thirdsAccountsDeleteRequest.getToken());

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // return a model with data...
        ThirdsAccountsUNAGRAResponse thirdsAccountsResponse = new ThirdsAccountsUNAGRAResponse()
                .builder()
                .origin(vgSourceUNAGRA)
                .dateTimeResponse(dateFormat.format(date))
                .thirdID(thirdsAccountsDeleteRequest.getThirdid())
                .msg("Se ha eliminado de manera exitosa la cuenta de tercero con identifiador "
                        + thirdsAccountsDeleteRequest.getThirdid())
                .build();

        // return dto to json...
        return thirdsAccountsResponse;
    }

    @Override
    public ThirdsAccountsListResponse getUNAGRAThirdsAccountsList(Long customerid, Integer pageNo, Integer pageSize) {
        // we need to validate if current customerid sent exists....
        Integer vlExists = thirdsAccountsRepository.findCustomerThirdsAccounts(customerid, 1);

        if (vlExists == 0) {
            throw new ResourceNotFoundException("El clienteID [" + customerid + "] no tiene cuenta de terceros | "
                    + vgSourceUNAGRA + " asociadas.");
        }

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // incluiding page properties...
        Sort sort = Sort.by("beneficiaryname").ascending();
        Pageable pageble = PageRequest.of(pageNo, pageSize, sort);
        Page<ThirdsAccounts> thirdsAccountsList = thirdsAccountsRepository.getThirdsAccountsList(customerid, 1,
                pageble);

        //if we have information...
        ThirdsAccountsListResponse thirdsAccountsListResponse = null;
        if (thirdsAccountsList != null) {
            // create a response body...
            thirdsAccountsListResponse = new ThirdsAccountsListResponse()
                    .builder()
                    .pageNo(pageble.getPageNumber() + 1)
                    .pageSize(pageble.getPageSize())
                    .recordsDisplayed(thirdsAccountsList.getNumberOfElements())
                    .recodsTotal(thirdsAccountsList.getTotalElements())
                    .recodsPages(thirdsAccountsList.getTotalPages())
                    .dateTimeResponse(dateFormat.format(date))
                    .thirdsAccountsList(thirdsAccountsList.getContent())
                    .msg("La página " + (pageble.getPageNumber() + 1) + " contiene "
                            + thirdsAccountsList.getNumberOfElements()
                            + " cuenta(s) de terceros " + vgSourceUNAGRA
                            + " asociadas al clienteID " + customerid)
                    .build();
        } else {
            thirdsAccountsListResponse = new ThirdsAccountsListResponse()
                    .builder()
                    .pageNo(pageble.getPageNumber())
                    .pageSize(pageble.getPageSize())
                    .recordsDisplayed(thirdsAccountsList.getNumberOfElements())
                    .recodsTotal(thirdsAccountsList.getTotalElements())
                    .recodsPages(thirdsAccountsList.getTotalPages())
                    .dateTimeResponse(dateFormat.format(date))
                    .thirdsAccountsList(thirdsAccountsList.getContent())
                    .msg("La página " + (pageble.getPageNumber() + 1) + " contiene "
                            + thirdsAccountsList.getNumberOfElements()
                            + " cuenta(s) de terceros " + vgSourceUNAGRA
                            + " asociadas al clienteID " + customerid)
                    .build();
        }

        //return data...
        return thirdsAccountsListResponse;

        // List<ThirdsAccountsDTO> finalArray = new ArrayList<>();

        /*
         *
         * // if current customer exists we can continues... List<String>
         * accountsDetailArray =
         * thirdsAccountsRepository.getUNAGRAThirdsAccountsList(customerid);
         * List<ThirdsAccountsDTO> finalArray = new ArrayList<>();
         *
         * // page creation... int start = (int) pageble.getOffset(); int end =
         * Math.min((start + pageble.getPageSize()), accountsDetailArray.size());
         *
         *
         * PagedListHolder pageHolder = new PagedListHolder(accountsDetailArray);
         * pageHolder.setPageSize(pageSize); // number of items per page
         * pageHolder.setPage(pageNo); // set to first page
         *
         * // get current dateTime action... DateFormat dateFormat = new
         * SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); Date date = new Date();
         *
         * // create a response body... ThirdsAccountsListResponse
         * thirdsAccountsListResponse = new ThirdsAccountsListResponse();
         * thirdsAccountsListResponse.setPageNo(pageHolder.getPageCount());
         * thirdsAccountsListResponse.setPageSize(pageHolder.getPageSize());
         * thirdsAccountsListResponse.setRecordsDisplayed(pageHolder.getPageList().size(
         * )); //
         * thirdsAccountsListResponse.setBankingInstitutionsToital(accountsDetailArray.
         * size());
         * thirdsAccountsListResponse.setDateTimeResponse(dateFormat.format(date));
         *
         * // Fill DTO From Array... // for(String accountDetailArray : accountsDetail)
         * { for (int i = 0; i < accountsDetailArray.size(); i++) { // get current
         * value... String vlMainData = accountsDetailArray.get(i);
         *
         * // applying split to get individual value of each record... String[] vlSplit
         * = vlMainData.split("[,]");
         *
         * // Adding to DTO... ThirdsAccountsDTO thirdsAccountsDTO = new
         * ThirdsAccountsDTO();
         * thirdsAccountsDTO.setThirdid(Long.parseLong(vlSplit[0]));
         * thirdsAccountsDTO.setYear(Integer.parseInt(vlSplit[1])); //
         * thirdsAccountsDTO.setRegistrationdate(vlSplit[2]); //
         * thirdsAccountsDTO.setRegistrationdatetime(vlSplit[3]);
         * thirdsAccountsDTO.setCustomerid(Long.parseLong(vlSplit[4]));
         * thirdsAccountsDTO.setAccountcard(vlSplit[5]);
         * thirdsAccountsDTO.setBeneficiaryname(vlSplit[6]);
         * thirdsAccountsDTO.setRfc(vlSplit[7]); thirdsAccountsDTO.setEmail(vlSplit[8]);
         * thirdsAccountsDTO.setTelephone(vlSplit[9]);
         * thirdsAccountsDTO.setBeneficiarybank(vlSplit[10]);
         * thirdsAccountsDTO.setInstitutionid(Long.parseLong(vlSplit[11]));
         * thirdsAccountsDTO.setNickname(vlSplit[12]);
         * thirdsAccountsDTO.setLimitamount(Double.parseDouble(vlSplit[13]));
         * thirdsAccountsDTO.setIstce(vlSplit[14]);
         * thirdsAccountsDTO.setIsunagra(Integer.parseInt(vlSplit[15]));
         *
         * finalArray.add(thirdsAccountsDTO); }
         */
    }

    @Override
    public ThirdsAccountsUNAGRAResponse updateUNAGRAThridAccount(ThirdsAccountsDTO thirdsAccountsDTO) {
        // if token sent doesn't valid, we can't continues...
        String vlValidateToken = otpTokenRepository.findTokenByCustomer(thirdsAccountsDTO.getCustomerid(),
                thirdsAccountsDTO.getToken());
        if (vlValidateToken == null) {
            throw new ResourceNotFoundException(
                    "El token proporcionado no tiene coincidencia con el proceso realizado por el clienteID ["
                            + thirdsAccountsDTO.getCustomerid().toString()
                            + "], favor de proporcionar el token correcto para esta solicitud.");
        }

        // search customer by ID...
        ThirdsAccounts thirdsAccounts = thirdsAccountsRepository.findById(thirdsAccountsDTO.getThirdid())
                .orElseThrow(() -> new ResourceNotFoundException("La cuenta de terceros | " + vgSourceUNAGRA
                        + " con ID [" + thirdsAccountsDTO.getThirdid() + "] no se encuentra."));

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // According data sent we need to add extra information to DTO...
        thirdsAccountsDTO.setLastupdate(date);
        switch (thirdsAccountsDTO.getAccountcard().trim().length()) {
            case 18: {
                thirdsAccountsDTO.setIstce("INTERBANK_ACCOUNT");
                break;
            }
            case 16: {
                thirdsAccountsDTO.setIstce("CARD");
                break;
            }
            default: {
                thirdsAccountsDTO.setIstce("INTERNAL_ACCOUNT");
                break;
            }
        }

        // update table from DTO...
        thirdsAccounts.setAccountcard(thirdsAccountsDTO.getAccountcard());
        thirdsAccounts.setBeneficiarybank(thirdsAccountsDTO.getBeneficiarybank());
        thirdsAccounts.setBeneficiaryname(thirdsAccountsDTO.getBeneficiaryname());
        thirdsAccounts.setEmail(thirdsAccountsDTO.getEmail());
        thirdsAccounts.setInstitutionid(thirdsAccountsDTO.getInstitutionid());
        thirdsAccounts.setIstce(thirdsAccountsDTO.getIstce());
        thirdsAccounts.setLastupdate(thirdsAccountsDTO.getLastupdate());
        thirdsAccounts.setLimitamount(thirdsAccountsDTO.getLimitamount());
        thirdsAccounts.setNickname(thirdsAccountsDTO.getNickname());
        thirdsAccounts.setRfc(thirdsAccountsDTO.getRfc());
        thirdsAccounts.setTelephone(thirdsAccountsDTO.getTelephone());
        thirdsAccounts.setThirdid(thirdsAccountsDTO.getThirdid());

        // update data...
        ThirdsAccounts thirdsAccountsUpdate = thirdsAccountsRepository.save(thirdsAccounts);

        // close token sent to prevent use it in others services...
        otpTokenRepository.updateStatusTokenByCustomer(thirdsAccountsDTO.getCustomerid(), thirdsAccountsDTO.getToken());

        // return result...
        ThirdsAccountsUNAGRAResponse thirdsAccountsResponse = new ThirdsAccountsUNAGRAResponse();
        thirdsAccountsResponse.setDateTimeResponse(dateFormat.format(date));
        thirdsAccountsResponse.setOrigin(vgSourceUNAGRA);
        thirdsAccountsResponse.setThirdID(thirdsAccountsDTO.getThirdid());
        thirdsAccountsResponse
                .setMsg("Se ha realizado la actualización de manera exitosa de la cuenta de terceros con id "
                        + thirdsAccountsResponse.getThirdID());
        return thirdsAccountsResponse;
    }

    @Override
    public ThirdsAccountsSPEIResponse newSPEIThirdAccount(ThirdsAccountsDTO thirdsAccountsDTO) {
        // if token sent doesn't valid, we can't continues...
        String vlValidateToken = otpTokenRepository.findTokenByCustomer(thirdsAccountsDTO.getCustomerid(),
                thirdsAccountsDTO.getToken());
        if (vlValidateToken == null) {
            throw new ResourceNotFoundException(
                    "El token proporcionado no tiene coincidencia con el proceso realizado por el clienteID ["
                            + thirdsAccountsDTO.getCustomerid().toString()
                            + "], favor de proporcionar el token correcto para esta solicitud.");
        }

        // According data sent we need to add extra information to DTO...
        thirdsAccountsDTO.setStatusid("v");
        thirdsAccountsDTO.setIsunagra(0);
        String vlKeyMatch = thirdsAccountsDTO.getAccountcard().trim().substring(0, 3);
        Integer vlIsCard = 0;
        switch (thirdsAccountsDTO.getAccountcard().trim().length()) {
            case 18: {
                thirdsAccountsDTO.setIstce("INTERBANK_ACCOUNT");
                break;
            }
            case 16: {
                thirdsAccountsDTO.setIstce("CARD");
                vlIsCard = 1;
                break;
            }
            default: {
                thirdsAccountsDTO.setIstce("INTERNAL_ACCOUNT");
                break;
            }
        }

        // if current account/card value is wrong, we can't continues...
        switch (thirdsAccountsDTO.getAccountcard().trim().length()) {
            case 16:
            case 18: {
                System.out.println("Account valid.");
                break;
            }
            default: {
                System.out.println("Account invalid.");
                throw new BadRequestException("La cuenta de terceros " + vgSourceSPEI
                        + ", no cumple la longitud para las transferencias SPEI, sólo es permitido ingresar la clabe interbancaria (18 dígitos) o bien el número de tarjeta (16 dígitos) del beneficiario.");
            }
        }

        //get bank information from current keymatch...
        //List<DetailBankInstitutionDTO> detailBank = null;
        //ResponseEntity<DetailBankInstitutionDTO> responseEntity = null;
        DetailBankInstitutionDTO detailBankInstitutionDTO;
        try {
            String baseUrl = "http://banking-institutions-api/bankinginstitutions/individual?keymatch=" + vlKeyMatch;
            //String baseUrl = "http://banking-institutions-api/bankinginstitutions/individual?keymatch=" + vlKeyMatch;

            detailBankInstitutionDTO = restTemplate.getForObject(baseUrl,DetailBankInstitutionDTO.class);


            /*
            HttpHeaders requestHeaders = new HttpHeaders();
            // set up HTTP Basic Authentication Header
            requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            //Request entity is created with request headers
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            //Rest Call to consume Rest Service
            responseEntity = restTemplate.exchange(baseUrl, HttpMethod.GET, requestEntity, DetailBankInstitutionDTO.class);

            logger.info(responseEntity.getBody().toString());
            */
            logger.info(detailBankInstitutionDTO.toString());


            //ResponseEntity<DetailBankInstitutionDTO> responseDetailBankingInstitution = restTemplate.getForEntity("http://localhost:6664/bankinginstitutions/individual?keymatch=" + vlKeyMatch, DetailBankInstitutionDTO.class);
            //ResponseEntity<DetailBankInstitutionDTO> responseDetailBankingInstitution = restTemplate.getForEntity(baseUrl, DetailBankInstitutionDTO.class);


            //ResponseEntity<DetailBankInstitutionDTO> detailBankInstitutionResponseEntity = restTemplate.getForObject("http://banking-institutions-api/bankinginstitutions/individual?keymatch=" + vlKeyMatch, DetailBankInstitutionDTO.class);
            //detailBank = restTemplate.getForObject(baseUrl, ArrayList.class);
            //ResponseEntity<DetailBankInstitution> = restTemplate.getForEntity("http://banking-institutions-api/bankinginstitutions/individual?keymatch=" + vlKeyMatch,DetailBankInstitution);
            //detailBankInstitutionClass = restTemplate.getForObject("http://banking-institutions-api/bankinginstitutions/individual?keymatch=" + vlKeyMatch, DetailBankInstitution.class);
            //logger.info("{}", detailBank);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("La clave interbancaria o el número de tarjeta ingresado, no tiene relación con una institución financiera valida, favor de revisar nuevamente la cuenta o tarjeta del beneficiario. \n" + ex.getMessage());
        }

        //response....


        //create a temporal dto to get information about current bank...
        ThirdsAccountsSPEIResponse thirdsAccountsSPEIResponse = new ThirdsAccountsSPEIResponse();
        //DetailBankInstitutionDTO detailBankInstitutionDTO = detailBankInstitutionDTO.getBody();
        //thirdsAccountsSPEIResponse.setDetailBank(detailBank);
        thirdsAccountsSPEIResponse.setDetailBank(detailBankInstitutionDTO);

        /*
        //get each value from arrayList...
        String vlJSON = thirdsAccountsSPEIResponse.getDetailBank().toString().replace("[", "").replace("]", "");
        Gson gsonValue = new Gson();
        DetailBankInstitutionDTO detailBankInstitution = gsonValue.fromJson(vlJSON, DetailBankInstitutionDTO.class);
        Long vlKeySPEI = Long.parseLong(detailBankInstitution.getCvespei().toString());
        String vlInstitution = detailBankInstitution.getInstitution();
        */
        logger.info("Instition: " + detailBankInstitutionDTO.getInstitution());
        logger.info("InsitutionID: " + detailBankInstitutionDTO.getCvespei());

        // we can't proccess acount by UNAGRA...
        /*
        if (vlKeySPEI.toString().equalsIgnoreCase(vpuUNAGRAID)) {
            System.out.println("UNAGRA Account invalid.");
            throw new BadRequestException("La cuenta de terceros " + vgSourceSPEI
                    + ", no puede contener la institución destino a UNAGRA, si desea capturar una cuenta de tercero perteneciente a UNAGRA use la opción de transferencias entre cuentas UNAGRA.");
        }
        */

        if (detailBankInstitutionDTO.getCvespei().toString().equalsIgnoreCase(vpuUNAGRAID)) {
            System.out.println("UNAGRA Account invalid.");
            throw new BadRequestException("La cuenta de terceros " + vgSourceSPEI
                    + ", no puede contener la institución destino a UNAGRA, si desea capturar una cuenta de tercero perteneciente a UNAGRA use la opción de transferencias entre cuentas UNAGRA.");
        }


        /*
        // validate if current bankid is available...
        bankingInstitutionsRepository.findById(thirdsAccountsDTO.getInstitutionid())
                .orElseThrow(() -> new ResourceNotFoundException("La institucionID ["
                        + thirdsAccountsDTO.getInstitutionid() + "] no se encuentra en nuestro catálogo."));
        */

        //adding information about current bank...
        //thirdsAccountsDTO.setInstitutionid(vlKeySPEI);
        //thirdsAccountsDTO.setBeneficiarybank(vlInstitution);
        thirdsAccountsDTO.setInstitutionid(Long.parseLong(detailBankInstitutionDTO.getCvespei().toString()));
        thirdsAccountsDTO.setBeneficiarybank(detailBankInstitutionDTO.getInstitution());

        // before to add this account we need to validate that doesn't exists...
        ThirdsAccounts vlExistsAccount = thirdsAccountsRepository.AccountExists(thirdsAccountsDTO.getCustomerid(),
                thirdsAccountsDTO.getAccountcard(), 0);
        if (vlExistsAccount != null) {
            throw new BadRequestException("¡Ups!, lo sentimos la cuentaID del beneficiario [" + thirdsAccountsDTO.getAccountcard() + "] ya ha sido registrada previamente.");
        }

        // convert DTO to Entity...
        ThirdsAccounts thirdsAccounts = mapToEntity(thirdsAccountsDTO);

        // Save Data...
        ThirdsAccounts newRecord = thirdsAccountsRepository.save(thirdsAccounts);

        // close token sent to prevent use it in others services...
        otpTokenRepository.updateStatusTokenByCustomer(thirdsAccountsDTO.getCustomerid(), thirdsAccountsDTO.getToken());

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // return a model with data...
        thirdsAccountsSPEIResponse.setOrigin(vgSourceSPEI);
        thirdsAccountsSPEIResponse.setDateTimeResponse(dateFormat.format(date));
        thirdsAccountsSPEIResponse.setThirdID(newRecord.getThirdid());
        thirdsAccountsSPEIResponse.setMsg("Se ha asociado de manera exitosa la cuenta de tercero "
                + thirdsAccountsDTO.getAccountcard() + " al clienteID " + thirdsAccountsDTO.getCustomerid());

        // return dto to json...
        return thirdsAccountsSPEIResponse;
    }

    @Override
    public ThirdsAccountsUNAGRAResponse deleteSPEIThridAccount(ThirdsAccountsDeleteRequest thirdsAccountsDeleteRequest) {
        // if token sent doesn't valid, we can't continues...
        String vlValidateToken = otpTokenRepository.findTokenByCustomer(thirdsAccountsDeleteRequest.getCustomerid(),
                thirdsAccountsDeleteRequest.getToken());
        if (vlValidateToken == null) {
            throw new ResourceNotFoundException(
                    "El token proporcionado no tiene coincidencia con el proceso realizado por el clienteID ["
                            + thirdsAccountsDeleteRequest.getCustomerid().toString()
                            + "], favor de proporcionar el token correcto para esta solicitud.");
        }

        // we need to find this id in our table to process...
        Integer vlResult = thirdsAccountsRepository.findThirdAccountById(thirdsAccountsDeleteRequest.getThirdid(), 0);
        if (vlResult == 0) {
            throw new NotFoundException("¡Ups!, la cuenta de terceros | " + vgSourceUNAGRA + " con ID ["
                    + thirdsAccountsDeleteRequest.getThirdid() + "] no se encuentra.");
        }
        /*
         * // we need to find this id in our table to process...
         * thirdsAccountsRepository.findById(thirdID).orElseThrow( () -> new
         * ResourceNotFoundException("Cuentas de Terceros | " + vgSourceSPEI, "thirID",
         * thirdID));
         */

        // delete record sent...
        thirdsAccountsRepository.deleteById(thirdsAccountsDeleteRequest.getThirdid());

        // close token sent to prevent use it in others services...
        otpTokenRepository.updateStatusTokenByCustomer(thirdsAccountsDeleteRequest.getCustomerid(),
                thirdsAccountsDeleteRequest.getToken());

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // return a model with data...
        ThirdsAccountsUNAGRAResponse thirdsAccountsResponse = new ThirdsAccountsUNAGRAResponse();
        thirdsAccountsResponse.setOrigin(vgSourceSPEI);
        thirdsAccountsResponse.setDateTimeResponse(dateFormat.format(date));
        thirdsAccountsResponse.setThirdID(thirdsAccountsDeleteRequest.getThirdid());
        thirdsAccountsResponse.setMsg("Se ha eliminado de manera exitosa la cuenta de tercero con identifiador "
                + thirdsAccountsDeleteRequest.getThirdid());

        // return dto to json...
        return thirdsAccountsResponse;
    }

    @Override
    public ThirdsAccountsListResponse getSPEIThirdsAccountsList(Long customerid, Integer pageNo, Integer pageSize) {
        // we need to validate if current customerid sent exists....
        Integer vlExists = thirdsAccountsRepository.findCustomerThirdsAccounts(customerid, 0);

        if (vlExists == 0) {
            throw new ResourceNotFoundException("El clienteID [" + customerid + "] no tiene cuenta de terceros | "
                    + vgSourceUNAGRA + " asociadas.");
        }

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // incluiding page properties...
        Sort sort = Sort.by("beneficiaryname").ascending();
        Pageable pageble = PageRequest.of(pageNo, pageSize, sort);
        Page<ThirdsAccounts> thirdsAccountsList = thirdsAccountsRepository.getThirdsAccountsList(customerid, 0,
                pageble);

        //if we have information...
        ThirdsAccountsListResponse thirdsAccountsListResponse = new ThirdsAccountsListResponse();
        if (thirdsAccountsList != null) {
            // create a response body...
            thirdsAccountsListResponse.setPageNo((pageble.getPageNumber() + 1));
            thirdsAccountsListResponse.setPageSize(pageble.getPageSize());
            thirdsAccountsListResponse.setRecordsDisplayed(thirdsAccountsList.getNumberOfElements());
            thirdsAccountsListResponse.setRecodsTotal(thirdsAccountsList.getTotalElements());
            thirdsAccountsListResponse.setRecodsPages(thirdsAccountsList.getTotalPages());
            thirdsAccountsListResponse.setDateTimeResponse(dateFormat.format(date));
            thirdsAccountsListResponse.setThirdsAccountsList(thirdsAccountsList.getContent());
        } else {
            // return a null value...
            thirdsAccountsListResponse.setPageNo(pageble.getPageNumber());
            thirdsAccountsListResponse.setPageSize(pageble.getPageSize());
            thirdsAccountsListResponse.setRecordsDisplayed(thirdsAccountsList.getNumberOfElements());
            thirdsAccountsListResponse.setRecodsTotal(thirdsAccountsList.getTotalElements());
            thirdsAccountsListResponse.setRecodsPages(thirdsAccountsList.getTotalPages());
            thirdsAccountsListResponse.setDateTimeResponse(dateFormat.format(date));
            thirdsAccountsListResponse.setThirdsAccountsList(thirdsAccountsList.getContent());
        }

        // adding final array...
        thirdsAccountsListResponse.setMsg(
                "La página " + (pageble.getPageNumber() + 1) + " contiene " + thirdsAccountsList.getNumberOfElements()
                        + " cuenta(s) de terceros " + vgSourceSPEI + " asociadas al clienteID " + customerid);

        //return data...
        return thirdsAccountsListResponse;
    }

    @Override
    public ThirdsAccountsUNAGRAResponse updateSPEIThridAccount(ThirdsAccountsDTO thirdsAccountsDTO) {
        // if token sent doesn't valid, we can't continues...
        String vlValidateToken = otpTokenRepository.findTokenByCustomer(thirdsAccountsDTO.getCustomerid(),
                thirdsAccountsDTO.getToken());
        if (vlValidateToken == null) {
            throw new ResourceNotFoundException(
                    "El token proporcionado no tiene coincidencia con el proceso realizado por el clienteID ["
                            + thirdsAccountsDTO.getCustomerid().toString()
                            + "], favor de proporcionar el token correcto para esta solicitud.");
        }

        // search customer by ID...
        ThirdsAccounts thirdsAccounts = thirdsAccountsRepository.findById(thirdsAccountsDTO.getThirdid())
                .orElseThrow(() -> new ResourceNotFoundException("La cuenta de terceros | " + vgSourceUNAGRA
                        + " con ID [" + thirdsAccountsDTO.getThirdid() + "] no se encuentra."));

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // According data sent we need to add extra information to DTO...
        thirdsAccountsDTO.setLastupdate(date);
        switch (thirdsAccountsDTO.getAccountcard().trim().length()) {
            case 18: {
                thirdsAccountsDTO.setIstce("INTERBANK_ACCOUNT");
                break;
            }
            case 16: {
                thirdsAccountsDTO.setIstce("CARD");
                break;
            }
            default: {
                thirdsAccountsDTO.setIstce("INTERNAL_ACCOUNT");
                break;
            }
        }

        // if current account/card value is wrong, we can't continues...
        switch (thirdsAccountsDTO.getAccountcard().trim().length()) {
            case 16:
            case 18: {
                System.out.println("Account valid.");
                break;
            }
            default: {
                System.out.println("Account invalid.");
                throw new BadRequestException("¡Ups!, la cuenta de terceros " + vgSourceSPEI
                        + ", no cumple la longitud para las transferencias SPEI, sólo es permitido ingresar la clabe interbancaria (18 dígitos) o bien el número de tarjeta (16 dígitos) del beneficiario.");
            }
        }

        // we can't proccess acount by UNAGRA...
        if (thirdsAccountsDTO.getInstitutionid().toString().equalsIgnoreCase(vpuUNAGRAID)) {
            System.out.println("UNAGRA Account invalid.");
            throw new BadRequestException("¡Ups!, la cuenta de terceros " + vgSourceSPEI
                    + ", no puede contener la institución destino a UNAGRA, si desea capturar una cuenta de tercero perteneciente a UNAGRA use la opción de transferencias entre cuentas UNAGRA.");
        }

        // validate if current bankid is available...
        bankingInstitutionsRepository.findById(thirdsAccountsDTO.getInstitutionid())
                .orElseThrow(() -> new ResourceNotFoundException("La institucionID ["
                        + thirdsAccountsDTO.getInstitutionid() + "] no se encuentra en nuestro catálogo."));

        // update table from DTO...
        thirdsAccounts.setAccountcard(thirdsAccountsDTO.getAccountcard());
        thirdsAccounts.setBeneficiarybank(thirdsAccountsDTO.getBeneficiarybank());
        thirdsAccounts.setBeneficiaryname(thirdsAccountsDTO.getBeneficiaryname());
        thirdsAccounts.setEmail(thirdsAccountsDTO.getEmail());
        thirdsAccounts.setInstitutionid(thirdsAccountsDTO.getInstitutionid());
        thirdsAccounts.setIstce(thirdsAccountsDTO.getIstce());
        thirdsAccounts.setLastupdate(thirdsAccountsDTO.getLastupdate());
        thirdsAccounts.setLimitamount(thirdsAccountsDTO.getLimitamount());
        thirdsAccounts.setNickname(thirdsAccountsDTO.getNickname());
        thirdsAccounts.setRfc(thirdsAccountsDTO.getRfc());
        thirdsAccounts.setTelephone(thirdsAccountsDTO.getTelephone());
        thirdsAccounts.setThirdid(thirdsAccountsDTO.getThirdid());

        // update data...
        ThirdsAccounts thirdsAccountsUpdate = thirdsAccountsRepository.save(thirdsAccounts);

        // close token sent to prevent use it in others services...
        otpTokenRepository.updateStatusTokenByCustomer(thirdsAccountsDTO.getCustomerid(), thirdsAccountsDTO.getToken());

        // return result...
        ThirdsAccountsUNAGRAResponse thirdsAccountsResponse = new ThirdsAccountsUNAGRAResponse();
        thirdsAccountsResponse.setDateTimeResponse(dateFormat.format(date));
        thirdsAccountsResponse.setOrigin(vgSourceSPEI);
        thirdsAccountsResponse.setThirdID(thirdsAccountsDTO.getThirdid());
        thirdsAccountsResponse
                .setMsg("Se ha realizado la actualización de manera exitosa de la cuenta de terceros con id "
                        + thirdsAccountsResponse.getThirdID());
        return thirdsAccountsResponse;
    }

    // convert Entity to DTO...
    private ThirdsAccountsDTO mapToDTO(ThirdsAccounts thirdsAccounts) {
        ThirdsAccountsDTO thirdsAccountsDTO = new ThirdsAccountsDTO();
        thirdsAccountsDTO.setAccountcard(thirdsAccounts.getAccountcard());
        thirdsAccountsDTO.setBeneficiarybank(thirdsAccounts.getBeneficiarybank());
        thirdsAccountsDTO.setBeneficiaryname(thirdsAccounts.getBeneficiaryname());
        thirdsAccountsDTO.setCustomerid(thirdsAccounts.getCustomerid());
        thirdsAccountsDTO.setEmail(thirdsAccounts.getEmail());
        thirdsAccountsDTO.setInstitutionid(thirdsAccounts.getInstitutionid());
        thirdsAccountsDTO.setIstce(thirdsAccounts.getIstce());
        thirdsAccountsDTO.setIsunagra(thirdsAccounts.getIsunagra());
        thirdsAccountsDTO.setLastupdate(thirdsAccounts.getLastupdate());
        thirdsAccountsDTO.setLimitamount(thirdsAccounts.getLimitamount());
        thirdsAccountsDTO.setNickname(thirdsAccounts.getNickname());
        thirdsAccountsDTO.setRegistrationdate(thirdsAccounts.getRegistrationdate());
        thirdsAccountsDTO.setRegistrationdatetime(thirdsAccounts.getRegistrationdatetime());
        thirdsAccountsDTO.setRfc(thirdsAccounts.getRfc());
        thirdsAccountsDTO.setStatusid(thirdsAccounts.getStatusid());
        thirdsAccountsDTO.setTelephone(thirdsAccounts.getTelephone());
        thirdsAccountsDTO.setThirdid(Long.parseLong("1"));
        thirdsAccountsDTO.setYear(thirdsAccounts.getYear());
        return thirdsAccountsDTO;
    }

    // convert DTO to Entity...
    private ThirdsAccounts mapToEntity(ThirdsAccountsDTO thirdsAccountsDTO) {
        ThirdsAccounts thirdsAccounts = new ThirdsAccounts();
        thirdsAccounts.setAccountcard(thirdsAccountsDTO.getAccountcard());
        thirdsAccounts.setBeneficiarybank(thirdsAccountsDTO.getBeneficiarybank());
        thirdsAccounts.setBeneficiaryname(thirdsAccountsDTO.getBeneficiaryname());
        thirdsAccounts.setCustomerid(thirdsAccountsDTO.getCustomerid());
        thirdsAccounts.setEmail(thirdsAccountsDTO.getEmail());
        thirdsAccounts.setInstitutionid(thirdsAccountsDTO.getInstitutionid());
        thirdsAccounts.setIstce(thirdsAccountsDTO.getIstce());
        thirdsAccounts.setIsunagra(thirdsAccountsDTO.getIsunagra());
        thirdsAccounts.setLastupdate(thirdsAccountsDTO.getLastupdate());
        thirdsAccounts.setLimitamount(thirdsAccountsDTO.getLimitamount());
        thirdsAccounts.setNickname(thirdsAccountsDTO.getNickname());
        thirdsAccounts.setRegistrationdate(thirdsAccountsDTO.getRegistrationdate());
        thirdsAccounts.setRegistrationdatetime(thirdsAccountsDTO.getRegistrationdatetime());
        thirdsAccounts.setRfc(thirdsAccountsDTO.getRfc());
        thirdsAccounts.setStatusid(thirdsAccountsDTO.getStatusid());
        thirdsAccounts.setTelephone(thirdsAccountsDTO.getTelephone());
        thirdsAccounts.setThirdid(Long.parseLong("1"));
        thirdsAccounts.setYear(thirdsAccountsDTO.getYear());
        return thirdsAccounts;
    }


}
