package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.OutgoingTransfersDTO;
import com.unagra.ebankingapi.models.TransactionKeyResponse;

public interface TransactionKeyService {
	public TransactionKeyResponse newTransactionKey(Integer noChars, OutgoingTransfersDTO outgoingTransfersDTO, String transactionDetailSealEncrypted);

}
