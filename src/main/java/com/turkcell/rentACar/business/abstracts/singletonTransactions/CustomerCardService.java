package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import java.util.List;

import com.turkcell.rentACar.business.dtos.customerCard.CustomerCardDto;
import com.turkcell.rentACar.business.dtos.customerCard.CustomerCardListDto;
import com.turkcell.rentACar.business.requests.customerCard.CreateCustomerCardRequest;
import com.turkcell.rentACar.business.requests.customerCard.DeleteCustomerCardRequest;
import com.turkcell.rentACar.business.requests.customerCard.UpdateCustomerCardRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface CustomerCardService {

    DataResult<List<CustomerCardListDto>> getAll();
	Result add(CreateCustomerCardRequest createCustomerCardRequest) throws BusinessException;
	DataResult<CustomerCardDto> getById(int id) throws BusinessException;
	Result update(UpdateCustomerCardRequest updateCustomerCardRequest) throws BusinessException;
	Result delete(DeleteCustomerCardRequest DeleteCustomerCardRequest) throws BusinessException;
    
}
