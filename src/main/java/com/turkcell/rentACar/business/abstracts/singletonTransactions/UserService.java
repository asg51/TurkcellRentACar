package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import com.turkcell.rentACar.business.requests.user.DeleteUserRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface UserService 
{
    Result delete(DeleteUserRequest deleteUserRequest) throws BusinessException;
}
