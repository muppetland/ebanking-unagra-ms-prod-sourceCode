package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.OutgoingTransfersDTO;
import com.unagra.ebankingapi.models.OutgoingTransfersResponse;

public interface OutgoingTransfersService {
	public OutgoingTransfersResponse newTransferUNAGRA_OWN(OutgoingTransfersDTO outgoingTransfersDTO);
	public OutgoingTransfersResponse newTransferUNAGRA_Thirds(OutgoingTransfersDTO outgoingTransfersDTO);

}
