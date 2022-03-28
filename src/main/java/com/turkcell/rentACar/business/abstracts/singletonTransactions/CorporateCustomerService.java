package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import java.util.List;

import com.turkcell.rentACar.business.dtos.corporateCustomer.CorporateCustomerDto;
import com.turkcell.rentACar.business.dtos.corporateCustomer.CorporateCustomerListDto;
import com.turkcell.rentACar.business.requests.corporateCustomer.CreateCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.corporateCustomer.DeleteCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.corporateCustomer.UpdateCorporateCustomerRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface CorporateCustomerService 
{   
    DataResult<List<CorporateCustomerListDto>> getAll();
    Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) throws BusinessException;
    DataResult<CorporateCustomerDto> getById(int id) throws BusinessException;
	Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest) throws BusinessException;
	Result delete(DeleteCorporateCustomerRequest DeleteCorporateCustomerRequest) throws BusinessException;
    Result checkIfExistByCorporateCustomerId(int corporateCustomerId) throws BusinessException;
}