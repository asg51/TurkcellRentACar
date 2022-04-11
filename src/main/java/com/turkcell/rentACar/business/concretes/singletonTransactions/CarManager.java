package com.turkcell.rentACar.business.concretes.singletonTransactions;

import com.turkcell.rentACar.business.abstracts.singletonTransactions.BrandService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.CarMaintenanceService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.CarService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.ColorService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.car.CarListDto;
import com.turkcell.rentACar.business.requests.car.CreateCarRequest;
import com.turkcell.rentACar.business.requests.car.DeleteCarRequest;
import com.turkcell.rentACar.business.requests.car.UpdateCarKilometerInformationRequest;
import com.turkcell.rentACar.business.requests.car.UpdateCarRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.CarDao;
import com.turkcell.rentACar.entities.concretes.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarManager implements CarService 
{
	private CarDao carDao;
	private ModelMapperService modelMapperService;

    private BrandService brandService;
    private ColorService colorService;

	@Autowired
	public CarManager(CarDao carDao, ModelMapperService modelMapperService, @Lazy CarMaintenanceService carMaintenanceService, BrandService brandService, ColorService colorService) 
	{
		this.carDao = carDao;
		this.modelMapperService = modelMapperService;

        this.brandService = brandService;
        this.colorService = colorService;
	}

	@Override
	public DataResult<List<CarListDto>> getAll() 
	{
		List<Car> result = this.carDao.findAll();
		List<CarListDto> response = result.stream().map(car->this.modelMapperService.forDto().map(car,CarListDto.class)).collect(Collectors.toList());
		
		return new SuccessDataResult<List<CarListDto>>(response,BusinessMessages.CAR_LISTED);
	}

	@Override
	public Result add(CreateCarRequest createCarRequest) throws BusinessException 
	{
        checkIfExistBrandId(createCarRequest.getBrandId());
        checkIfExistByColorId(createCarRequest.getColorId());

		Car car = this.modelMapperService.forRequest().map(createCarRequest,Car.class);
		this.carDao.save(car);
		
		return new SuccessResult(BusinessMessages.CAR_ADDED);
	}

	@Override
	public Result update(UpdateCarRequest updateCarRequest) throws BusinessException
	{
		checkIfExistByCarId(updateCarRequest.getCarId());
        checkIfExistBrandId(updateCarRequest.getBrandId());
        checkIfExistByColorId(updateCarRequest.getColorId());
		
		Car car = carDao.getById(updateCarRequest.getCarId());
		car = this.modelMapperService.forRequest().map(updateCarRequest,Car.class);
		this.carDao.save(car);
		
		return new SuccessResult(BusinessMessages.CAR_UPDATED);
	}

    @Override
    public Result updateCarKilometerInformation(UpdateCarKilometerInformationRequest updateCarKilometerInformationRequest) throws BusinessException 
    {
        checkIfExistByCarId(updateCarKilometerInformationRequest.getCarId());

		Car car = this.carDao.getById(updateCarKilometerInformationRequest.getCarId());
		car.setKilometerInformation(updateCarKilometerInformationRequest.getNewKilometer());
		this.carDao.save(car);

		return new SuccessResult(BusinessMessages.CAR_UPDATED);
    }

	@Override
	public Result delete(DeleteCarRequest deleteCarRequest) throws BusinessException 
	{
		checkIfExistByCarId(deleteCarRequest.getCarId());
		
		Car car = this.modelMapperService.forRequest().map(deleteCarRequest,Car.class);
		this.carDao.delete(car);
		
		return new SuccessResult(BusinessMessages.CAR_DELETED);
	}

	@Override
	public DataResult<CarListDto> getById(int id) throws BusinessException 
	{
		checkIfExistByCarId(id);
		
		Car car = this.carDao.getById(id);
		CarListDto carListDto = this.modelMapperService.forDto().map(car,CarListDto.class);
		
		return new SuccessDataResult<CarListDto>(carListDto,BusinessMessages.CAR_GETTED);
	}

	@Override
	public DataResult<List<CarListDto>> getByCarDailyPriceLessThanOrEqual(Double carDailyPrice) 
	{
		Sort sort = Sort.by(Sort.Direction.DESC,"carDailyPrice");
		List<Car> result = this.carDao.getByCarDailyPriceLessThanEqual(carDailyPrice,sort);
		List<CarListDto> response = result.stream().map(car->this.modelMapperService.forDto().map(car,CarListDto.class)).collect(Collectors.toList());
		
		return new SuccessDataResult<List<CarListDto>>(response,BusinessMessages.CAR_LISTED);
	}

	@Override
	public DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) 
	{
		Pageable pageable = PageRequest.of(pageNo,pageSize);
		List<Car> result = this.carDao.findAll();
		List<CarListDto> response = result.stream().map(car->this.modelMapperService.forDto().map(car,CarListDto.class)).collect(Collectors.toList());
		
		return new SuccessDataResult<List<CarListDto>>(response,BusinessMessages.CAR_LISTED);
	}

	@Override
	public DataResult<List<CarListDto>> getAllSorted(Sort.Direction direction) 
	{
		Sort sort = Sort.by(direction,"carDailyPrice");
		List<Car> result =this.carDao.findAll(sort);
		List<CarListDto> response = result.stream().map(car->this.modelMapperService.forDto().map(car,CarListDto.class)).collect(Collectors.toList());
		
		return new SuccessDataResult<List<CarListDto>>(response,BusinessMessages.CAR_LISTED);
	}

	@Override
	public Result checkIfExistByCarId(int carId) throws BusinessException 
	{
		if(!this.carDao.existsById(carId)) 
		{
			throw new BusinessException(BusinessMessages.CAR_NOT_FOUND);
		}
		return new SuccessResult(BusinessMessages.CAR_FOUND);
	}

    private void checkIfExistBrandId(int brandId) throws BusinessException 
	{
		this.brandService.checkIfExistBrandId(brandId);
	}

    private void checkIfExistByColorId(int colorId) throws BusinessException
    {
        this.colorService.checkIfExistByColorId(colorId);
    }
}
