package com.unagra.ebankingapi.service;

import java.util.List;
import com.unagra.ebankingapi.dto.LoginDTO;
import com.unagra.ebankingapi.models.LoginResponse;
import com.unagra.ebankingapi.models.PasswordRecoveredResponse;

public interface LoginViewService {
	public List<LoginDTO> getLoginData();

	public LoginDTO getValueByCustomerID(Long customerID);

	public LoginResponse loginCustomer(Long customerID, String password);

	public PasswordRecoveredResponse recoveryPassword(Long customerID, String email, String password, String token);
}
