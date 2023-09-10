package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.models.MovementsByCustomerHistoryResponse;
import com.unagra.ebankingapi.models.MovementsByCustomerIndexResponse;

public interface MovementsByCustomerService {
    MovementsByCustomerIndexResponse findMovementsByCustomerIndex(Long vpAccountID);

    MovementsByCustomerHistoryResponse findMovementsByCustomerHistory(Long vpAccountID, Integer pageNo, Integer pageSize, Integer filterOption, String startDate, String endDate);
}
