package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.entities.ebanking.OtpToken;
import com.unagra.ebankingapi.exceptions.ResourceNotFoundException;
import com.unagra.ebankingapi.models.HSMResponse;
import com.unagra.ebankingapi.repository.ebanking.HSMRepository;
import com.unagra.ebankingapi.repository.ebanking.OtpTokenRepository;
import com.unagra.ebankingapi.utils.AES.AlgorithAESCBC;
import com.unagra.ebankingapi.utils.HSM.ValidateOTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class HSMServiceImp implements HSMService {
    @Value("${ebanking.enviormentClass}")
    public String vpuClassEnviorment;
    @Value("${ebanking.enviormentOPT}")
    public Integer vpuEnviormentOPT;
    @Value("${ebanking.vector}")
    public String vpuVector;
    @Value("${ebanking.password}")
    public String vpuKey;

    @Autowired
    private HSMRepository hsmRepository;
    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Override
    public HSMResponse matchOTP(Long customerID, String OTP, Integer moduleid) {
        // getting path to invoke the otp service...
        String getValuesParams = hsmRepository.getURLEnviorment(vpuClassEnviorment, vpuEnviormentOPT);

        // we will invoke endpoint to validate information send...
        ValidateOTP validateOTP = new ValidateOTP();
        Integer vlResponseCode = validateOTP.validarOTP(customerID, OTP, getValuesParams);

        if (vlResponseCode != 200) {
            throw new ResourceNotFoundException("¡Ups!, lo sentimos, el OTP proporcionado para el clienteID [" + customerID + "] no se ha podido validar de manera exitosa.");
        }

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        Date date = new Date();

        // generate token from current informataion...
        String vlDataToConvert = customerID.toString() + OTP.toString() + moduleid.toString() + dateFormat.format(date);
        AlgorithAESCBC vlAESData = new AlgorithAESCBC();

        // validate if current token doesn't existe in our table...
        boolean vlTokenExists = true;
        Integer vlCount = 0;
        String vlTokenEncrypted = "";
        String vlOtpEncrypted = "";
        while ( vlTokenExists == true ) {
            vlTokenEncrypted = vlAESData.resultadoEncriptado((vlDataToConvert + vlCount.toString()), vpuKey, vpuVector);
            vlOtpEncrypted = vlAESData.resultadoEncriptado(OTP.toString(), vpuKey, vpuVector);

            // search token generated...
            String vlSearchToken = otpTokenRepository.findByToken(vlTokenEncrypted);
            if (vlSearchToken == null) {
                // we can continues...
                System.out.println("OTP generado para la aceptacicón del proceso: " + vlTokenEncrypted);
                vlTokenExists = false;
            }

            // inc counter...
            vlCount = vlCount++;
        }

        // save current dataEncrypted...
        OtpToken vlData = new OtpToken();
        vlData.setCustomerid(customerID);
        vlData.setModuleid(moduleid);
        vlData.setOtp(vlOtpEncrypted);
        vlData.setStatusid("v");
        vlData.setYear(Integer.parseInt(dateFormatYear.format(date)));
        vlData.setEventdate(date);
        vlData.setEventdatetime(date);
        vlData.setToken(vlTokenEncrypted);
        otpTokenRepository.save(vlData);

        // return hsmResponse...
        HSMResponse hsmResponse = new HSMResponse().builder()
                .customerid(customerID)
                .eventdatetime(dateFormat.format(date))
                .token(vlTokenEncrypted).msg("El OTP proporcionado para la cuenta [" + customerID.toString() + "] se ha validado de manera correcta.")
                .responsecode(200).build();
        return hsmResponse;
    }
}
