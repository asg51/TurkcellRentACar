package com.turkcell.rentACar.business.concretes.singletonTransactions;

import java.util.List;
import java.util.stream.Collectors;

import com.turkcell.rentACar.business.abstracts.singletonTransactions.CarCrashInformationService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.CarService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.carCrashInformation.CarCrashInformationDto;
import com.turkcell.rentACar.business.dtos.carCrashInformation.CarCrashInformationListDto;
import com.turkcell.rentACar.business.requests.carCrashInformation.CreateCarCrashInformationRequest;
import com.turkcell.rentACar.business.requests.carCrashInformation.DeleteCarCrashInformationRequest;
import com.turkcell.rentACar.business.requests.carCrashInformation.UpdateCarCrashInformationRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarCrashInformationDao;
import com.turkcell.rentACar.entities.concretes.CarCrashInformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CarCrashInformationManager implements CarCrashInformationService 
{
    private CarCrashInformationDao carCrashInformationDao;
    private ModelMapperService modelMapperService;
    private CarService carService;

    @Autowired
    public CarCrashInformationManager(CarCrashInformationDao carCrashInformationDao, ModelMapperService modelMapperService, @Lazy CarService carService) 
    {
        this.carCrashInformationDao = carCrashInformationDao;
        this.modelMapperService = modelMapperService;
        this.carService = carService;
    }

    @Override
    public Result add(CreateCarCrashInformationRequest createCarCrashInformationRequest) throws BusinessException 
    {
        checkIfCarExists(createCarCrashInformationRequest.getCarId());

        CarCrashInformation carCrashInformation = this.modelMapperService.forRequest().map(createCarCrashInformationRequest, CarCrashInformation.class);
        carCrashInformation.setCarCrashInformationId(0);

        this.carCrashInformationDao.save(carCrashInformation);

        return new SuccessResult(BusinessMessages.CAR_CRASH_INFORMATION_ADDED);
    }

    @Override
    public Result delete(DeleteCarCrashInformationRequest deleteCarCrashInformationRequest) throws BusinessException 
    {
        checkIfCarCrashInformatioExists(deleteCarCrashInformationRequest.getCarCrashInformationId());

        CarCrashInformation carCrashInformation = this.modelMapperService.forRequest().map(deleteCarCrashInformationRequest, CarCrashInformation.class);

        this.carCrashInformationDao.deleteById(carCrashInformation.getCarCrashInformationId());

        return new SuccessResult(BusinessMessages.CAR_CRASH_INFORMATION_DELETED);
    }

    @Override
    public DataResult<CarCrashInformationDto> getByCarCrashInformationId(int carCrashInformationId) throws BusinessException 
    {
        checkIfCarCrashInformatioExists(carCrashInformationId);

        CarCrashInformation result = this.carCrashInformationDao.getByCarCrashInformationId(carCrashInformationId);

        CarCrashInformationDto response = this.modelMapperService.forDto().map(result, CarCrashInformationDto.class);

        return new SuccessDataResult<CarCrashInformationDto>(response, BusinessMessages.CAR_CRASH_INFORMATION_GETTED);
    }

    @Override
    public DataResult<List<CarCrashInformationListDto>> getAll() 
    {
        List<CarCrashInformation> result = this.carCrashInformationDao.findAll();

        List<CarCrashInformationListDto> response = result.stream()
                .map(carCrashInformation -> this.modelMapperService.forDto().map(carCrashInformation, CarCrashInformationListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CarCrashInformationListDto>>(response, BusinessMessages.CAR_CRASH_INFORMATION_LISTED);
    }

    @Override
    public DataResult<List<CarCrashInformationListDto>> getAllPaged(int pageNo, int pageSize) 
    {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<CarCrashInformation> result = this.carCrashInformationDao.findAll(pageable).getContent();

        List<CarCrashInformationListDto> response = result.stream()
                .map(carCrashInformation -> this.modelMapperService.forDto().map(carCrashInformation, CarCrashInformationListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CarCrashInformationListDto>>(response,BusinessMessages.CAR_CRASH_INFORMATION_LISTED);
    }

    @Override
    public DataResult<List<CarCrashInformationListDto>> getAllSorted(Sort.Direction direction) 
    {
        Sort sort = Sort.by(direction, "carCrashInformationId");

        List<CarCrashInformation> result = this.carCrashInformationDao.findAll(sort);

        List<CarCrashInformationListDto> response = result.stream()
                .map(carCrashInformation -> this.modelMapperService.forDto().map(carCrashInformation, CarCrashInformationListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CarCrashInformationListDto>>(response, BusinessMessages.CAR_CRASH_INFORMATION_LISTED);
    }

    @Override
    public Result update(UpdateCarCrashInformationRequest updateCarCrashInformationRequest) throws BusinessException 
    {
        checkIfCarCrashInformatioExists(updateCarCrashInformationRequest.getCarCrashInformationId());
        checkIfCarExists(updateCarCrashInformationRequest.getCarId());

        CarCrashInformation carCrashInformationUpdate = this.modelMapperService.forRequest().map(updateCarCrashInformationRequest, CarCrashInformation.class);

        this.carCrashInformationDao.save(carCrashInformationUpdate);

        return new SuccessResult(BusinessMessages.CAR_CRASH_INFORMATION_UPDATED);
    }

    private void checkIfCarCrashInformatioExists(int carCrashInformationId) throws BusinessException 
    {
        if(this.carCrashInformationDao.getByCarCrashInformationId(carCrashInformationId) == null)
        {
            throw new BusinessException(BusinessMessages.CAR_CRASH_INFORMATION_ALREADY_EXISTS);
        }
    }
    
    private void checkIfCarExists(int carId) throws BusinessException 
    {
        this.carService.checkIfExistByCarId(carId);
    }
}