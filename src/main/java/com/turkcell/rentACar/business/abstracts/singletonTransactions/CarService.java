package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import com.turkcell.rentACar.business.dtos.car.CarListDto;
import com.turkcell.rentACar.business.requests.car.CreateCarRequest;
import com.turkcell.rentACar.business.requests.car.DeleteCarRequest;
import com.turkcell.rentACar.business.requests.car.UpdateCarRequest;
import com.turkcell.rentACar.business.requests.car.UpdateCarKilometerInformationRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CarService 
{
    DataResult<List<CarListDto>> getAll();
    Result add(CreateCarRequest createCarRequest) throws BusinessException;
    Result update(UpdateCarRequest updateCarRequest) throws BusinessException;
    Result updateCarKilometerInformation(UpdateCarKilometerInformationRequest updateCarKilometerInformationRequest) throws BusinessException;
    Result delete(DeleteCarRequest deleteCarRequest) throws BusinessException;
    DataResult<CarListDto> getById(int id) throws BusinessException;
    DataResult<List<CarListDto>> getByCarDailyPriceLessThanOrEqual(Double carDailyPrice);
    DataResult<List<CarListDto>> getAllPaged(int pageNo,int pageSize);
    DataResult<List<CarListDto>> getAllSorted(Sort.Direction direction);
    Result checkIfExistByCarId(int carId) throws BusinessException;
    
}
