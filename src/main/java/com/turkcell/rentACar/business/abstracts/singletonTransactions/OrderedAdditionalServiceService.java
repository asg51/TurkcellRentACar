package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import java.util.List;

import com.turkcell.rentACar.business.dtos.orderedAdditionalService.OrderedAdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalService.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.orderedAdditionalService.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.orderedAdditionalService.DeleteOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.orderedAdditionalService.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface OrderedAdditionalServiceService 
{
    Result add(CreateOrderedAdditionalServiceRequest createAdditionalServiceRequest) throws BusinessException;
    Result addServices(List<Integer> additionalServiceIds, int carRentalId) throws BusinessException;
    Result update(UpdateOrderedAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException;
    Result delete(DeleteOrderedAdditionalServiceRequest deleteAdditionalServiceRequest) throws BusinessException;
    Result deleteAllByCarRentelId(int carRentelId) throws BusinessException;
    DataResult<List<OrderedAdditionalServiceListDto>> getAll();
    DataResult<OrderedAdditionalServiceDto> getById(int additionalServiceId) throws BusinessException;
    DataResult<List<OrderedAdditionalServiceListDto>> getAllByOrderedAdditionalService_CarRentalId(int carRentalId) throws BusinessException;
}
