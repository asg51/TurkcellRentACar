package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import java.util.List;

import com.turkcell.rentACar.business.dtos.additionalService.AdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.additionalService.AdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.additionalService.CalculateAdditionalServiceForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.additionalService.CalculateAdditionalServiceForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.additionalService.CreateAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.additionalService.DeleteAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.additionalService.UpdateAdditionalServiceRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface AdditionalServiceService 
{
    Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest) throws BusinessException;
    Result update(UpdateAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException;
    Result delete(DeleteAdditionalServiceRequest deleteAdditionalServiceRequest) throws BusinessException;
    DataResult<List<AdditionalServiceListDto>> getAll();
    DataResult<AdditionalServiceDto> getById(int additionalServiceId) throws BusinessException;
    DataResult<List<AdditionalServiceListDto>> getAdditionalServicesByIds(List<Integer> additionalServicesIds) throws BusinessException;
    DataResult<Double> calculateAdditionalServicePriceForCorporateCustomer(CalculateAdditionalServiceForCorporateCustomerRequest calculateAdditionalServiceForCorporateCustomerRequest) throws BusinessException;
    DataResult<Double> calculateAdditionalServicePriceForIndividualCustomer(CalculateAdditionalServiceForIndividualCustomerRequest calculateAdditionalServiceForIndividualCustomerRequest) throws BusinessException;

    Result checkIfExistByAdditionalServiceById(int additionalServiceId) throws BusinessException;
}
