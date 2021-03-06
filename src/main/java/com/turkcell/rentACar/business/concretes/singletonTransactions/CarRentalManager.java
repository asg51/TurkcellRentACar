package com.turkcell.rentACar.business.concretes.singletonTransactions;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.turkcell.rentACar.business.abstracts.singletonTransactions.CarMaintenanceService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.CarRentalService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.CarService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.CorporateCustomerService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.IndividualCustomerService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.car.CarListDto;
import com.turkcell.rentACar.business.dtos.carRental.CarRentalDto;
import com.turkcell.rentACar.business.dtos.carRental.CarRentalListDto;
import com.turkcell.rentACar.business.requests.carRental.CalculatePriceCarRentalForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.CalculatePriceCarRentalForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.CalculatePriceLateReturnTheCarRentalForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.CalculatePriceLateReturnTheCarRentalForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.CreateCarRentalForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.CreateCarRentalForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.DeleteCarRentalForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.DeleteCarRentalForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.LateReturnTheRentalCarForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.LateReturnTheRentalCarForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.ReturnTheRentalCarForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.ReturnTheRentalCarForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.UpdateCarRentalForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.UpdateCarRentalForIndividualCustomerRequest;
import com.turkcell.rentACar.business.utilities.dateOperations;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarRentalDao;
import com.turkcell.rentACar.entities.concretes.CarRental;

@Service
public class CarRentalManager implements CarRentalService 
{
	private ModelMapperService modelMapperService;
	private CarRentalDao carRentalDao;
	private CarMaintenanceService carMaintenanceService;
	private CarService carService;
	private IndividualCustomerService individualCustomerService;
	private CorporateCustomerService corporateCustomerService;

	@Autowired
	public CarRentalManager(ModelMapperService modelMapperService, 
			CarRentalDao carRentalDao,
			@Lazy CarMaintenanceService carMaintenanceService, 
			CarService carService,
			IndividualCustomerService individualCustomerService, 
			CorporateCustomerService corporateCustomerService) 
	{
		this.modelMapperService = modelMapperService;
		this.carRentalDao = carRentalDao;
		this.carMaintenanceService = carMaintenanceService;
		this.carService = carService;

		this.individualCustomerService = individualCustomerService;
		this.corporateCustomerService = corporateCustomerService;
	}

	@Override
	public DataResult<List<CarRentalListDto>> getAll() 
	{
		List<CarRental> result = this.carRentalDao.findAll();

		List<CarRentalListDto> response = result.stream()
				.map(carRental -> this.modelMapperService.forDto().map(carRental, CarRentalListDto.class))
				.collect(Collectors.toList());
				
		return new SuccessDataResult<List<CarRentalListDto>>(response, BusinessMessages.CAR_RENTAL_LISTED);
	}

	@Override
	public DataResult<CarRentalDto> rentForIndividualCustomer(CreateCarRentalForIndividualCustomerRequest createCarRentalForIndividualCustomerRequest) throws BusinessException 
	{
		checkIfCarExistsById(createCarRentalForIndividualCustomerRequest.getCarId());
		checkIfCarMaintenance(createCarRentalForIndividualCustomerRequest.getCarId());
		checkIfItCanBeRented(createCarRentalForIndividualCustomerRequest.getStartDate(), createCarRentalForIndividualCustomerRequest.getReturnDate(), createCarRentalForIndividualCustomerRequest.getCarId());
		checkIfReturnDateAfterStartDate(createCarRentalForIndividualCustomerRequest.getStartDate(), createCarRentalForIndividualCustomerRequest.getReturnDate());
		checkIfStartDateBeforeToday(createCarRentalForIndividualCustomerRequest.getStartDate());
		checkIfIndividualCustomerExists(createCarRentalForIndividualCustomerRequest.getIndividualCustomerId());


		CarRental carRental = this.modelMapperService.forRequest().map(createCarRentalForIndividualCustomerRequest, CarRental.class);
		carRental.setCarRentalId(0);
		carRental.setReturnStatus(false);
		carRental.getCustomer().setUserId(createCarRentalForIndividualCustomerRequest.getIndividualCustomerId());

		carRental = this.carRentalDao.save(carRental);

		CarRentalDto carRentalDto= this.modelMapperService.forDto().map(carRental, CarRentalDto.class);

		return new SuccessDataResult<CarRentalDto>(carRentalDto,BusinessMessages.CAR_RENTAL_ADDED);
	}

	@Override
	public DataResult<CarRentalDto> rentForCorporateCustomer(
		CreateCarRentalForCorporateCustomerRequest createCarRentalForCorporateCustomerRequest) throws BusinessException 
	{
		checkIfCarExistsById(createCarRentalForCorporateCustomerRequest.getCarId());
		checkIfCarMaintenance(createCarRentalForCorporateCustomerRequest.getCarId());
		checkIfItCanBeRented(createCarRentalForCorporateCustomerRequest.getStartDate(), createCarRentalForCorporateCustomerRequest.getReturnDate(), createCarRentalForCorporateCustomerRequest.getCarId());
		checkIfReturnDateAfterStartDate(createCarRentalForCorporateCustomerRequest.getStartDate(), createCarRentalForCorporateCustomerRequest.getReturnDate());
		checkIfStartDateBeforeToday(createCarRentalForCorporateCustomerRequest.getStartDate());
		checkIfCorporateCustomerExists(createCarRentalForCorporateCustomerRequest.getCorporateCustomerId());

		CarRental carRental = this.modelMapperService.forRequest().map(createCarRentalForCorporateCustomerRequest, CarRental.class);
		carRental.setCarRentalId(0);
		carRental.setReturnStatus(false);
		carRental.getCustomer().setUserId(createCarRentalForCorporateCustomerRequest.getCorporateCustomerId());

		carRental = this.carRentalDao.save(carRental);

		CarRentalDto carRentalDto= this.modelMapperService.forDto().map(carRental, CarRentalDto.class);

		return new SuccessDataResult<CarRentalDto>(carRentalDto,BusinessMessages.CAR_RENTAL_ADDED);
	}

    @Override
    public DataResult<CarRentalDto> returnTheRentalCarForCorporateCustomer(ReturnTheRentalCarForCorporateCustomerRequest returnTheRentalCarForCorporateCustomerRequest) throws BusinessException 
    {
        checkIfCorporateCustomerExists(returnTheRentalCarForCorporateCustomerRequest.getCorporateCustomerId());
		checkIfCarRentalExistsById(returnTheRentalCarForCorporateCustomerRequest.getCarRentalId());

		CarRental carRental = this.carRentalDao.getById(returnTheRentalCarForCorporateCustomerRequest.getCarRentalId());

		checkIfItIsReturned(carRental.getCarRentalId());
        checkForLateReturn(carRental.getReturnDate(),returnTheRentalCarForCorporateCustomerRequest.getReturnDate());
		checkIfTheCustomerIsTheSame(returnTheRentalCarForCorporateCustomerRequest.getCorporateCustomerId(), carRental.getCustomer().getCustomerId());

		carRental.setReturnKilometer(returnTheRentalCarForCorporateCustomerRequest.getReturnKilometer());
		carRental.setReturnStatus(true);

		carRental = this.carRentalDao.save(carRental);

        CarRentalDto carRentalDto= this.modelMapperService.forDto().map(carRental, CarRentalDto.class);

		return new SuccessDataResult<CarRentalDto>(carRentalDto,BusinessMessages.CAR_RENTAL_RETURNED);
    }

    @Override
    public DataResult<CarRentalDto> returnTheRentalCarForIndividualCustomer(ReturnTheRentalCarForIndividualCustomerRequest returnTheRentalCarForIndividualCustomerRequest) throws BusinessException 
    {
        checkIfIndividualCustomerExists(returnTheRentalCarForIndividualCustomerRequest.getIndividualCustomerId());
		checkIfCarRentalExistsById(returnTheRentalCarForIndividualCustomerRequest.getCarRentalId());

		CarRental carRental = this.carRentalDao.getById(returnTheRentalCarForIndividualCustomerRequest.getCarRentalId());

		checkIfItIsReturned(carRental.getCarRentalId());
        checkForLateReturn(carRental.getReturnDate(),returnTheRentalCarForIndividualCustomerRequest.getReturnDate());
		checkIfTheCustomerIsTheSame(returnTheRentalCarForIndividualCustomerRequest.getIndividualCustomerId(), carRental.getCustomer().getCustomerId());

		carRental.setReturnKilometer(returnTheRentalCarForIndividualCustomerRequest.getReturnKilometer());
		carRental.setReturnStatus(true);

		carRental = this.carRentalDao.save(carRental);

        CarRentalDto carRentalDto= this.modelMapperService.forDto().map(carRental, CarRentalDto.class);

		return new SuccessDataResult<CarRentalDto>(carRentalDto,BusinessMessages.CAR_RENTAL_RETURNED);
    }

    @Override
    public DataResult<CarRentalDto> lateReturnTheRentalCarForCorporateCustomer(LateReturnTheRentalCarForCorporateCustomerRequest lateReturnTheRentalCarForCorporateCustomerRequest) throws BusinessException 
    {
        checkIfCorporateCustomerExists(lateReturnTheRentalCarForCorporateCustomerRequest.getCorporateCustomerId());
		checkIfCarRentalExistsById(lateReturnTheRentalCarForCorporateCustomerRequest.getCarRentalId());

		CarRental carRental = this.carRentalDao.getById(lateReturnTheRentalCarForCorporateCustomerRequest.getCarRentalId());

		checkIfItIsReturned(carRental.getCarRentalId());
		checkIfTheCustomerIsTheSame(lateReturnTheRentalCarForCorporateCustomerRequest.getCorporateCustomerId(), carRental.getCustomer().getCustomerId());

		carRental.setReturnKilometer(lateReturnTheRentalCarForCorporateCustomerRequest.getReturnKilometer());
        carRental.setReturnDate(lateReturnTheRentalCarForCorporateCustomerRequest.getReturnDate());
		carRental.setReturnStatus(true);

		carRental = this.carRentalDao.save(carRental);

        CarRentalDto carRentalDto= this.modelMapperService.forDto().map(carRental, CarRentalDto.class);

		return new SuccessDataResult<CarRentalDto>(carRentalDto,BusinessMessages.CAR_RENTAL_LATE_RETURNED);
    }

    @Override
    public DataResult<CarRentalDto> lateReturnTheRentalCarForIndividualCustomer(LateReturnTheRentalCarForIndividualCustomerRequest lateReturnTheRentalCarForIndividualCustomerRequest) throws BusinessException 
    {
        checkIfIndividualCustomerExists(lateReturnTheRentalCarForIndividualCustomerRequest.getIndividualCustomerId());
		checkIfCarRentalExistsById(lateReturnTheRentalCarForIndividualCustomerRequest.getCarRentalId());

		CarRental carRental = this.carRentalDao.getById(lateReturnTheRentalCarForIndividualCustomerRequest.getCarRentalId());

		checkIfItIsReturned(carRental.getCarRentalId());
		checkIfTheCustomerIsTheSame(lateReturnTheRentalCarForIndividualCustomerRequest.getIndividualCustomerId(), carRental.getCustomer().getCustomerId());

		carRental.setReturnKilometer(lateReturnTheRentalCarForIndividualCustomerRequest.getReturnKilometer());
        carRental.setReturnDate(lateReturnTheRentalCarForIndividualCustomerRequest.getReturnDate());
		carRental.setReturnStatus(true);

		carRental = this.carRentalDao.save(carRental);

        CarRentalDto carRentalDto= this.modelMapperService.forDto().map(carRental, CarRentalDto.class);

		return new SuccessDataResult<CarRentalDto>(carRentalDto,BusinessMessages.CAR_RENTAL_LATE_RETURNED);
    }

	@Override
	public Result updateForCorporateCustomer(UpdateCarRentalForCorporateCustomerRequest updateCarRentalForCorporateCustomerRequest) throws BusinessException 
	{
		checkIfCarExistsById(updateCarRentalForCorporateCustomerRequest.getCarId());
		checkIfCarRentalExistsById(updateCarRentalForCorporateCustomerRequest.getCarRentalId());
		checkIfItCanBeRented(updateCarRentalForCorporateCustomerRequest.getStartDate(), updateCarRentalForCorporateCustomerRequest.getReturnDate(), updateCarRentalForCorporateCustomerRequest.getCarId(),updateCarRentalForCorporateCustomerRequest.getCarRentalId());
		checkIfReturnDateAfterStartDate(updateCarRentalForCorporateCustomerRequest.getStartDate(), updateCarRentalForCorporateCustomerRequest.getReturnDate());
		checkIfStartDateBeforeToday(updateCarRentalForCorporateCustomerRequest.getStartDate());
		checkIfCorporateCustomerExists(updateCarRentalForCorporateCustomerRequest.getCorporateCustomerId());

		double startingKilometer = this.carRentalDao.getById(updateCarRentalForCorporateCustomerRequest.getCarRentalId()).getStartingKilometer(); 

		CarRental carRental = this.modelMapperService.forRequest().map(updateCarRentalForCorporateCustomerRequest, CarRental.class);
		carRental.setStartingKilometer(startingKilometer);
		carRental.getCustomer().setUserId(updateCarRentalForCorporateCustomerRequest.getCorporateCustomerId());

		carRental =	this.carRentalDao.save(carRental);
		
		return new SuccessResult(BusinessMessages.CAR_RENTAL_UPDATED);
	}

	@Override
	public Result updateForIndividualCustomer(UpdateCarRentalForIndividualCustomerRequest updateCarRentalForIndividualCustomerRequest) throws BusinessException 
	{
		checkIfCarExistsById(updateCarRentalForIndividualCustomerRequest.getCarId());
		checkIfCarRentalExistsById(updateCarRentalForIndividualCustomerRequest.getCarRentalId());
		checkIfItCanBeRented(updateCarRentalForIndividualCustomerRequest.getStartDate(), updateCarRentalForIndividualCustomerRequest.getReturnDate(), updateCarRentalForIndividualCustomerRequest.getCarId(),updateCarRentalForIndividualCustomerRequest.getCarRentalId());
		checkIfReturnDateAfterStartDate(updateCarRentalForIndividualCustomerRequest.getStartDate(), updateCarRentalForIndividualCustomerRequest.getReturnDate());
		checkIfStartDateBeforeToday(updateCarRentalForIndividualCustomerRequest.getStartDate());
		checkIfIndividualCustomerExists(updateCarRentalForIndividualCustomerRequest.getIndividualCustomerId());

		double startingKilometer = this.carRentalDao.getById(updateCarRentalForIndividualCustomerRequest.getCarRentalId()).getStartingKilometer(); 

		CarRental carRental = this.modelMapperService.forRequest().map(updateCarRentalForIndividualCustomerRequest, CarRental.class);
		carRental.setStartingKilometer(startingKilometer);
		carRental.getCustomer().setUserId(updateCarRentalForIndividualCustomerRequest.getIndividualCustomerId());

		this.carRentalDao.save(carRental);

		return new SuccessResult(BusinessMessages.CAR_RENTAL_UPDATED);
	}

	@Override
	public Result deleteForIndividualCustomer(DeleteCarRentalForIndividualCustomerRequest deleteCarRentalForIndividualCustomerRequest) throws BusinessException 
	{
		checkIfCarRentalExistsById(deleteCarRentalForIndividualCustomerRequest.getCarRentalId());
		
		this.carRentalDao.deleteById(deleteCarRentalForIndividualCustomerRequest.getCarRentalId());
		
		return new SuccessResult(BusinessMessages.CAR_RENTAL_DELETED);
	}

	@Override
	public Result deleteForCorporateCustomer(DeleteCarRentalForCorporateCustomerRequest deleteCarRentalForCorporateCustomerRequest) throws BusinessException 
	{
		checkIfCarRentalExistsById(deleteCarRentalForCorporateCustomerRequest.getCarRentalId());
		
		CarRental carRental = this.modelMapperService.forRequest().map(deleteCarRentalForCorporateCustomerRequest, CarRental.class);
		this.carRentalDao.deleteById(carRental.getCarRentalId());
		
		return new SuccessResult(BusinessMessages.CAR_RENTAL_DELETED);
	}

	@Override
	public DataResult<List<CarRentalListDto>> getByCarId(int id) throws BusinessException 
	{
		checkIfCarExistsById(id);
		
		List<CarRental> result = this.carRentalDao.getAllByCar_CarId(id);
		List<CarRentalListDto> response = result.stream()
				.map(carRental -> this.modelMapperService.forDto().map(carRental, CarRentalListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CarRentalListDto>>(response, BusinessMessages.CAR_RENTAL_LISTED);
	}

	@Override
	public Result IsACarAvailableOnTheSpecifiedDate(int carId, LocalDate returnDate) throws BusinessException 
	{
		checkIfCarExistsById(carId);
		
		var result = this.carRentalDao.getRentalInformationOfTheCarOnTheSpecifiedDate(returnDate, returnDate,carId);

		if (result.size() > 0) 
		{
			throw new BusinessException(BusinessMessages.CAR_RENTAL_ALREADY_EXISTS_ON_SPECIFIC_DATE);
		}
		return new SuccessResult(BusinessMessages.CAR_RENTAL_NOT_FOUND_ON_SPECIFIC_DATE);
	}

	@Override
	public DataResult<Double> calculatePriceCarRentalForCorporateCustomer(CalculatePriceCarRentalForCorporateCustomerRequest calculatePriceCarRentalForCorporateCustomerRequest) throws BusinessException
	{
		var result = calculatePrice(calculatePriceCarRentalForCorporateCustomerRequest.getCarId(), calculatePriceCarRentalForCorporateCustomerRequest.getStartDate(), calculatePriceCarRentalForCorporateCustomerRequest.getReturnDate(),
		calculatePriceCarRentalForCorporateCustomerRequest.getStartCityId(),
		calculatePriceCarRentalForCorporateCustomerRequest.getEndCityId());

		return new SuccessDataResult<Double>(result,BusinessMessages.CAR_RENTAL_CHARGE_CALCULATED);
	}

	@Override
	public DataResult<Double> calculatePriceCarRentalForIndividualCustomer(CalculatePriceCarRentalForIndividualCustomerRequest calculatePriceCarRentalForIndividualCustomerRequest) throws BusinessException
	{
		var result = calculatePrice(calculatePriceCarRentalForIndividualCustomerRequest.getCarId(), calculatePriceCarRentalForIndividualCustomerRequest.getStartDate(), calculatePriceCarRentalForIndividualCustomerRequest.getReturnDate(),
		calculatePriceCarRentalForIndividualCustomerRequest.getStartCityId(),
		calculatePriceCarRentalForIndividualCustomerRequest.getEndCityId());

		return new SuccessDataResult<Double>(result,BusinessMessages.CAR_RENTAL_CHARGE_CALCULATED);
	}

    @Override
    public DataResult<Double> calculatePriceLateReturnTheCarRentalForIndividualCustomer(CalculatePriceLateReturnTheCarRentalForIndividualCustomerRequest calculatePriceLateReturnTheCarRentalForIndividualCustomerRequest) throws BusinessException 
    {
        var result = calculatePriceForLateReturn(calculatePriceLateReturnTheCarRentalForIndividualCustomerRequest.getCarId(), calculatePriceLateReturnTheCarRentalForIndividualCustomerRequest.getExpectedDate(), calculatePriceLateReturnTheCarRentalForIndividualCustomerRequest.getReturnDate());

		return new SuccessDataResult<Double>(result,BusinessMessages.CAR_RENTAL_LATE_RETURN_CHARGE_CALCULATED);
    }

    @Override
    public DataResult<Double> calculatePriceLateReturnTheCarRentalForCorporateCustomer(CalculatePriceLateReturnTheCarRentalForCorporateCustomerRequest calculatePriceLateReturnTheCarRentalForCorporateCustomerRequest) throws BusinessException 
    {
        var result = calculatePriceForLateReturn(calculatePriceLateReturnTheCarRentalForCorporateCustomerRequest.getCarId(), calculatePriceLateReturnTheCarRentalForCorporateCustomerRequest.getExpectedDate(), calculatePriceLateReturnTheCarRentalForCorporateCustomerRequest.getReturnDate());

		return new SuccessDataResult<Double>(result,BusinessMessages.CAR_RENTAL_LATE_RETURN_CHARGE_CALCULATED);
    }

	@Override
	public DataResult<CarRentalDto> getById(int id) 
	{
		CarRental carRental = this.carRentalDao.getById(id);
		CarRentalDto carRentalDto = this.modelMapperService.forDto().map(carRental,CarRentalDto.class);
		
		return new SuccessDataResult<CarRentalDto>(carRentalDto,BusinessMessages.CAR_RENTAL_GETTED);
	}

	@Override
	public Result checkIfCarRentalExistsById(int carRentalId) throws BusinessException 
	{
		var result=	this.carRentalDao.existsByCarRentalId(carRentalId);
		if(result == false)
		{
			throw new BusinessException(BusinessMessages.CAR_RENTAL_NOT_FOUND);
		}

		return new SuccessResult(BusinessMessages.CAR_RENTAL_FOUND);
	}

	private double calculatePrice(int carId, LocalDate startDate, LocalDate returnDate, int startCityId, int endCityId) throws BusinessException
	{	
		CarListDto car = this.carService.getById(carId).getData();

		long days = dateOperations.findTheNumberOfDaysToRent(startDate, returnDate);

		double price = days * car.getCarDailyPrice() + calculateExtraPriceByCityDistance(startCityId, endCityId);
        
		return price;
	}

    private double calculatePriceForLateReturn(int carId, LocalDate startDate, LocalDate returnDate) throws BusinessException
	{	
		CarListDto car = this.carService.getById(carId).getData();

		long days = dateOperations.findTheNumberOfDaysToRent(startDate, returnDate);

		double price = days * car.getCarDailyPrice();

		return price;
	}

	private void checkIfItCanBeRented(LocalDate startDate, LocalDate returnDate, int carId) throws BusinessException 
	{
		var result = this.carRentalDao.getRentalInformationOfTheCarOnTheSpecifiedDate(startDate, returnDate, carId);
		if (result.size() > 0) 
		{
			throw new BusinessException(BusinessMessages.CAR_RENTAL_ALREADY_EXISTS_ON_SPECIFIC_DATE);
		}
	}

	private void checkIfItCanBeRented(LocalDate startDate, LocalDate returnDate, int carId,int carRentalId) throws BusinessException 
	{
		var result = this.carRentalDao.getRentalInformationOfTheCarOnTheSpecifiedDate(startDate, returnDate, carId, carRentalId);
		if (result.size() > 0) 
		{
			throw new BusinessException(BusinessMessages.CAR_RENTAL_ALREADY_EXISTS_ON_SPECIFIC_DATE);
		}
	}

	private void checkIfCarMaintenance(int carId) throws BusinessException 
	{
		this.carMaintenanceService.checkIfItIsMaintainableByMaintenance(carId);
	}

	private double calculateExtraPriceByCityDistance(int startCityId, int endCityId) 
	{
		if (startCityId==endCityId) 
		{
			return 0;
		}
		return 750;
	}
	
	private void checkIfCarExistsById(int carId) throws BusinessException 
	{
		this.carService.checkIfExistByCarId(carId);
	}

	private void checkIfStartDateBeforeToday(LocalDate startDate) throws BusinessException
	{	
		LocalDate now = LocalDate.now();

		if(startDate.isBefore(now))
		{
			throw new BusinessException(BusinessMessages.START_DATE_MUST_NOT_BE_BEFORE_TODAY);
		}
	}

	private void checkIfReturnDateAfterStartDate(LocalDate startDate, LocalDate returnDate) throws BusinessException
	{	
		if(startDate.isAfter(returnDate))
		{
			throw new BusinessException(BusinessMessages.START_DATE_MUST_NOT_BE_BEFORE_RETURN_DATE);
		}
	}

	private void checkIfCorporateCustomerExists(int corporateCustomerId) throws BusinessException
	{
		this.corporateCustomerService.checkIfExistByCorporateCustomerId(corporateCustomerId);
	}

	private void checkIfIndividualCustomerExists(int individualCustomerId) throws BusinessException
	{
		this.individualCustomerService.checkIfExistByIndividualCustomerId(individualCustomerId);
	}

	private void checkIfTheCustomerIsTheSame(int customerId1, int customerId2) throws BusinessException
	{
		if(customerId1!=customerId2)
		{
			throw new BusinessException(BusinessMessages.CAR_RENTAL_CUSTOMER_IS_NOT_THE_SAME);
		}
	}

    private void checkForLateReturn(LocalDate expectedDate, LocalDate incomingDate) throws BusinessException
    {
       if(expectedDate.isBefore(incomingDate))
       {
           throw new BusinessException(BusinessMessages.CAR_RENTAL_MADE_LATE_RETURN);
       }
	}

    private void checkIfItIsReturned(int rentACarId) throws BusinessException
    {
       CarRental carRental = carRentalDao.getById(rentACarId);

	   if(carRental.isReturnStatus())
	   {
			throw new BusinessException(BusinessMessages.CAR_RENTAL_MADE_RETURN);
	   }
    }
}