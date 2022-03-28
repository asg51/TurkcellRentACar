package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import com.turkcell.rentACar.business.dtos.carCrashInformation.CarCrashInformationDto;
import com.turkcell.rentACar.business.dtos.carCrashInformation.CarCrashInformationListDto;
import com.turkcell.rentACar.business.requests.carCrashInformation.CreateCarCrashInformationRequest;
import com.turkcell.rentACar.business.requests.carCrashInformation.DeleteCarCrashInformationRequest;
import com.turkcell.rentACar.business.requests.carCrashInformation.UpdateCarCrashInformationRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CarCrashInformationService 
{
    Result add(CreateCarCrashInformationRequest createCarCrashInformationRequest) throws BusinessException;

    DataResult<CarCrashInformationDto> getByCarCrashInformationId(int carCrashInformationId) throws BusinessException;

    DataResult<List<CarCrashInformationListDto>> getAll();

    DataResult<List<CarCrashInformationListDto>> getAllPaged(int pageNo, int pageSize);

    DataResult<List<CarCrashInformationListDto>> getAllSorted(Sort.Direction direction);

    Result update(UpdateCarCrashInformationRequest updateCarCrashInformationRequest) throws BusinessException;

    Result delete(DeleteCarCrashInformationRequest deleteCarCrashInformationRequest) throws BusinessException;
}