package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.CatchmentMovementsDTO;

public interface CatchmentMovementsService {
	public CatchmentMovementsDTO getDataFromMovementID(Long movementID); 

}
