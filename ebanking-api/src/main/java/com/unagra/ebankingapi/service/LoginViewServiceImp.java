package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.LoginDTO;
import com.unagra.ebankingapi.entities.ebanking.Login;
import com.unagra.ebankingapi.exceptions.*;
import com.unagra.ebankingapi.models.LoginResponse;
import com.unagra.ebankingapi.models.PasswordRecoveredResponse;
import com.unagra.ebankingapi.repository.ebanking.LoginRepository;
import com.unagra.ebankingapi.repository.ebanking.OtpTokenRepository;
import com.unagra.ebankingapi.utils.AES.AlgorithAESCBC;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.nio.file.AccessDeniedException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginViewServiceImp implements LoginViewService {
    @Value("${ebanking.vector}")
    public String vpuVector;
    @Value("${ebanking.password}")
    public String vpuKey;

    @Autowired
    private LoginRepository loginViewRepository;

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Override
    public List<LoginDTO> getLoginData() {
        List<Login> loginDetail = loginViewRepository.findAll();

        //if we don't have information...
        if (loginDetail == null) {
            throw new RuntimeException("¡Ups!, lo sentimos, al parecer no contamos con información relacionada al clienteID [" + loginDetail.get(0) + "]");
        }

        return loginDetail.stream().map(loginView -> mapToDTO(loginView)).collect(Collectors.toList());
    }

    @Override
    public LoginDTO getValueByCustomerID(Long customerID) {
        Login login = loginViewRepository.findById(customerID).orElseThrow(() -> new ResourceNotFoundException("!Ups¡, no tenemos información del clienteID [" + customerID + "], valide la información proporcionada."));
        return mapToDTO(login);
    }

    @Override
    public LoginResponse loginCustomer(Long customerID, String password) {
        Login login = loginViewRepository.findById(customerID).orElseThrow(() -> new ResourceNotFoundException("!Ups¡, no tenemos información del clienteID[" + customerID + "], valide la información proporcionada."));

        // we need to decrypt info...
        AlgorithAESCBC algorithAESCBC = new AlgorithAESCBC();
        String vlPasswordDesencriptado = algorithAESCBC.resultadoDesencriptado(password, vpuKey, vpuVector);

        // match password?...
        Login authLogin = loginViewRepository.matchPassword(customerID, password);

        if (authLogin == null) {
            throw new ResourceAccessException("¡Ups!, las credenciales del inicio de sesión del clienteID [" + customerID.toString() + "] no son validas.");
        } else {
            // user locked?...
            if (authLogin.getLocked() == 1) {
                throw new ResourceAccessException("¡Ups!, lo sentimos, el clienteID [" + customerID + "] se encuentra bloqueado, es necesario contactar al área encargada que realizará la validación de sus datos de acceso, esto lo podrá realizar enviando un email a la siguiente dirección: customerservice@unagra.com.mx o bien, podrá acudir a su sucursal más cercana.");
            } else {
                // we need to lock account?...
                if (authLogin.getFailedattempts() > 3) {
                    throw new ResourceAccessException("¡Ups!, lo sentimos, el clienteID [" + customerID + "] se encuentra bloqueado, es necesario contactar al área encargada que realizará la validación de sus datos de acceso, esto lo podrá realizar enviando un email a la siguiente dirección: customerservice@unagra.com.mx o bien, podrá acudir a su sucursal más cercana.");
                } else {
                    // customer needs to change password?...
                    if (authLogin.getRequestchangepassword() == 1) {
                        throw new ResourceAccessException("\"El clienteID [" + customerID + "] deberá realizar cambio de contraseña para poder continuar con el inicio de sesion en el servicio de Banca por Ineternet.");
                    }
                }
            }
        }

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // return result...
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setCustomerid(authLogin.getCustomerid());
        loginResponse.setCustomerName(login.getCustomername());
        loginResponse.setEmail(login.getEmail());
        loginResponse.setEventdatetime(dateFormat.format(date));
        loginResponse.setMsg("Los datos de inicio de sesión de la cuenta [" + customerID.toString() + "] han sido procesados de manera correcta.");
        loginResponse.setResponsecode(200);
        return loginResponse;
    }

    @Override
    public PasswordRecoveredResponse recoveryPassword(Long customerID, String email, String password, String token) {
        // if token sent doesn't valid, we can't continues...
        String vlValidateToken = otpTokenRepository.findTokenByCustomer(customerID, token);
        if (vlValidateToken == null) {
            throw new ResourceNotFoundException("El token proporcionado no tiene coincidencia con el proceso realizado por el clienteID [" + customerID.toString() + "], favor de proporcionar el token correcto para esta solicitud.");
        }

        // search customer by ID...
        Login login = loginViewRepository.findById(customerID).orElseThrow(() -> new ResourceNotFoundException("No tenemos información del clienteID [" + customerID + "], valide la información proporcionada."));

        /*
         * if we have a table entity we can use this method... // Modify only fields
         * that we need in DTO... login.setPassword(loginDTO.getPassword());
         *
         * // Update record in Entity... Login recordUpdated =
         * loginViewRepository.save(login);
         */

        // current email match?...
        Login authLogin = loginViewRepository.matchEmail(customerID, email);

        if (authLogin == null) {
            throw new ResourceNotFoundException("La cuenta de correo [" + email + "] no está asociada al clienteID [" + customerID + "]");
        } else {
            // update password...
            loginViewRepository.updatePassword(password, customerID);
        }

        // close token sent to prevent use it in others services...
        otpTokenRepository.updateStatusTokenByCustomer(customerID, token);

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // return result...
        PasswordRecoveredResponse passwordRecoveredResponse = new PasswordRecoveredResponse();
        passwordRecoveredResponse.setCustomerid(authLogin.getCustomerid());
        passwordRecoveredResponse.setEventdatetime(dateFormat.format(date));
        passwordRecoveredResponse.setMsg("Se ha realizado el cambio de password para la cuenta [" + customerID.toString() + "] de manera exitosa.");
        passwordRecoveredResponse.setResponsecode(200);
        return passwordRecoveredResponse;
    }

    // convert Entity to DTO...
    private LoginDTO mapToDTO(Login loginView) {
        LoginDTO loginViewDTO = new LoginDTO();
        loginViewDTO.setRequestchangepassword(loginView.getRequestchangepassword());
        loginViewDTO.setCustomerid(loginView.getCustomerid());
        loginViewDTO.setCustomername(loginView.getCustomername());
        loginViewDTO.setFailedAttempts(loginView.getFailedattempts());
        loginViewDTO.setLocked(loginView.getLocked());
        loginViewDTO.setPassword(loginView.getPassword());
        loginViewDTO.setStatusId(loginView.getStatusid());
        return loginViewDTO;
    }

    // convert DTO to Entity...
    private Login mapToEntity(LoginDTO loginViewDTO) {
        Login loginView = new Login();
        loginView.setRequestchangepassword(loginViewDTO.getRequestchangepassword());
        loginView.setCustomerid(loginViewDTO.getCustomerid());
        loginView.setCustomername(loginViewDTO.getCustomername());
        loginView.setFailedattempts(loginViewDTO.getFailedAttempts());
        loginView.setLocked(loginViewDTO.getLocked());
        loginView.setPassword(loginViewDTO.getPassword());
        loginView.setStatusid(loginViewDTO.getStatusId());
        return loginView;
    }
}
