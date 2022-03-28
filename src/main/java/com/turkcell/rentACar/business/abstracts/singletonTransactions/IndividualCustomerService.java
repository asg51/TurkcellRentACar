package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import java.util.List;

import com.turkcell.rentACar.business.dtos.individualCustomer.IndividualCustomerDto;
import com.turkcell.rentACar.business.dtos.individualCustomer.IndividualCustomerListDto;
import com.turkcell.rentACar.business.requests.individualCustomer.CreateIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.individualCustomer.DeleteIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.individualCustomer.UpdateIndividualCustomerRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface IndividualCustomerService 
{   
    DataResult<List<IndividualCustomerListDto>> getAll();
    Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) throws BusinessException;
    DataResult<IndividualCustomerDto> getById(int id) throws BusinessException;
	Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws BusinessException;
	Result delete(DeleteIndividualCustomerRequest DeleteIndividualCustomerRequest) throws BusinessException;
    Result checkIfExistByIndividualCustomerId(int individualCustomerId) throws BusinessException;
}