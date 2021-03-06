package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface CustomerService 
{
    Result checkIfExistByCustomerId(int customerId) throws BusinessException;    
}
