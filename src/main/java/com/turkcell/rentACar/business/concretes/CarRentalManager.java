package com.turkcell.rentACar.business.concretes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.turkcell.rentACar.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACar.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACar.business.abstracts.CarRentalService;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.abstracts.InvoiceService;
import com.turkcell.rentACar.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACar.business.dtos.CarListDto;
import com.turkcell.rentACar.business.dtos.CarRentalDto;
import com.turkcell.rentACar.business.dtos.CarRentalListDto;
import com.turkcell.rentACar.business.requests.creates.CreateCarRentalForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.creates.CreateCarRentalForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.creates.CreateInvoiceRequest;
import com.turkcell.rentACar.business.requests.creates.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.deletes.DeleteCarRentalForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.deletes.DeleteCarRentalForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.updates.UpdateCarRentalForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.updates.UpdateCarRentalForIndividualCustomerRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarRentalDao;
import com.turkcell.rentACar.entities.concretes.CarRental;

@Service
public class CarRentalManager implements CarRentalService {

	private ModelMapperService modelMapperService;
	private CarRentalDao carRentalDao;
	private CarMaintenanceService carMaintenanceService;
	private CarService carService;
	private AdditionalServiceService additionalServiceService;
	private OrderedAdditionalServiceService orderedAdditionalServiceService;
	private InvoiceService invoiceService;

	@Autowired
	public CarRentalManager(ModelMapperService modelMapperService, 
			CarRentalDao carRentalDao,
			@Lazy CarMaintenanceService carMaintenanceService, 
			CarService carService,
			AdditionalServiceService additionalServiceService,
			@Lazy OrderedAdditionalServiceService orderedAdditionalServiceService,
			InvoiceService invoiceService) 
	{
		this.modelMapperService = modelMapperService;
		this.carRentalDao = carRentalDao;
		this.carMaintenanceService = carMaintenanceService;
		this.carService = carService;
		this.additionalServiceService = additionalServiceService;
		this.orderedAdditionalServiceService = orderedAdditionalServiceService;
		this.invoiceService= invoiceService;
	}

	@Override
	public DataResult<List<CarRentalDto>> getAll() {
		List<CarRental> result = this.carRentalDao.findAll();
		List<CarRentalDto> response = result.stream()
				.map(carRental -> this.modelMapperService.forDto().map(carRental, CarRentalDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CarRentalDto>>(response, "Car Rental Listed Successfully");
	}

	@Override
	public Result rentForIndividualCustomer(
			CreateCarRentalForIndividualCustomerRequest createCarRentalForIndividualCustomerRequest) throws BusinessException 
	{
		checkIfCarExistsById(createCarRentalForIndividualCustomerRequest.getCarId());
		checkIfCarMaintenance(createCarRentalForIndividualCustomerRequest.getCarId());
		checkIfItCanBeRented(createCarRentalForIndividualCustomerRequest.getStartDate(), createCarRentalForIndividualCustomerRequest.getReturnDate(), createCarRentalForIndividualCustomerRequest.getCarId());

		var price =  calculatePriceForIndividaulCustomer(createCarRentalForIndividualCustomerRequest).getData();

		CarRental carRental = this.modelMapperService.forRequest().map(createCarRentalForIndividualCustomerRequest, CarRental.class);
		carRental.setCarRentalId(0);
		carRental.setPrice(price);
		carRental.setRentedDays(findTheNumberOfDaysToRent(carRental.getStartDate(), carRental.getReturnDate()));

		carRental = this.carRentalDao.save(carRental);

		insertAddtionalServices(createCarRentalForIndividualCustomerRequest.getAdditionalServiceIds(),carRental.getCarRentalId());
		insertInvoice(createCarRentalForIndividualCustomerRequest.getIndividualCustomerId(), price, carRental);

		return new SuccessResult("Car Rental Added Successfully");
	}

	@Override
	public Result rentForCorporateCustomer(
		CreateCarRentalForCorporateCustomerRequest createCarRentalForCorporateCustomerRequest) throws BusinessException 
	{
		checkIfCarExistsById(createCarRentalForCorporateCustomerRequest.getCarId());
		checkIfCarMaintenance(createCarRentalForCorporateCustomerRequest.getCarId());
		checkIfItCanBeRented(createCarRentalForCorporateCustomerRequest.getStartDate(), createCarRentalForCorporateCustomerRequest.getReturnDate(), createCarRentalForCorporateCustomerRequest.getCarId());

		var price = calculatePriceForCorporateCustomer(createCarRentalForCorporateCustomerRequest).getData();

		CarRental carRental = this.modelMapperService.forRequest().map(createCarRentalForCorporateCustomerRequest, CarRental.class);
		carRental.setCarRentalId(0);
		carRental.setPrice(price);
		carRental.setRentedDays(findTheNumberOfDaysToRent(carRental.getStartDate(), carRental.getReturnDate()));

		carRental=	this.carRentalDao.save(carRental);

		insertAddtionalServices(createCarRentalForCorporateCustomerRequest.getAdditionalServiceIds(),carRental.getCarRentalId());
		insertInvoice(createCarRentalForCorporateCustomerRequest.getCorporateCustomerId(), price, carRental);

		return new SuccessResult("Car Rental Added Successfully");
	}

	@Override
	public Result updateForCorporateCustomer(UpdateCarRentalForCorporateCustomerRequest updateCarRentalForCorporateCustomerRequest) throws BusinessException 
	{
		checkIfCarExistsById(updateCarRentalForCorporateCustomerRequest.getCarId());
		checkIfCarRentalExistsById(updateCarRentalForCorporateCustomerRequest.getCarRentalId());
		checkIfItCanBeRented(updateCarRentalForCorporateCustomerRequest.getStartDate(), updateCarRentalForCorporateCustomerRequest.getReturnDate(), updateCarRentalForCorporateCustomerRequest.getCarId(),updateCarRentalForCorporateCustomerRequest.getCarRentalId());

		CarRental carRental = this.modelMapperService.forRequest().map(updateCarRentalForCorporateCustomerRequest, CarRental.class);
		this.carRentalDao.save(carRental);
		
		return new SuccessResult("Car Rental Updated Successfully");
	}

	@Override
	public Result updateForIndividualCustomer(UpdateCarRentalForIndividualCustomerRequest updateCarRentalForIndividualCustomerRequest) throws BusinessException 
	{
		checkIfCarExistsById(updateCarRentalForIndividualCustomerRequest.getCarId());
		checkIfCarRentalExistsById(updateCarRentalForIndividualCustomerRequest.getCarRentalId());
		checkIfItCanBeRented(updateCarRentalForIndividualCustomerRequest.getStartDate(), updateCarRentalForIndividualCustomerRequest.getReturnDate(), updateCarRentalForIndividualCustomerRequest.getCarId(),updateCarRentalForIndividualCustomerRequest.getCarRentalId());

		this.orderedAdditionalServiceService.deleteAllByCarRentelId(updateCarRentalForIndividualCustomerRequest.getCarRentalId());
		insertAddtionalServices(updateCarRentalForIndividualCustomerRequest.getAdditionalServiceIds(),updateCarRentalForIndividualCustomerRequest.getCarRentalId());

//////////////////// maplenecek
		
		var price = calculatePriceForCorporateCustomer(updateCarRentalForIndividualCustomerRequest).getData();

		this.invoiceService.update(updateInvoiceRequest)

		CarRental carRental = this.modelMapperService.forRequest().map(updateCarRentalForIndividualCustomerRequest, CarRental.class);
		this.carRentalDao.save(carRental);



		return new SuccessResult("Car Rental Updated Successfully");
	}

	@Override
	public Result deleteForIndividualCustomer(DeleteCarRentalForIndividualCustomerRequest deleteCarRentalForIndividualCustomerRequest) throws BusinessException 
	{
		checkIfCarRentalExistsById(deleteCarRentalForIndividualCustomerRequest.getCarRentalId());
		
		CarRental carRental = this.modelMapperService.forRequest().map(deleteCarRentalForIndividualCustomerRequest, CarRental.class);
		this.carRentalDao.deleteById(carRental.getCarRentalId());
		
		return new SuccessResult("Car Rental Deleted Successfully");
	}

	@Override
	public Result deleteForCorporateCustomer(DeleteCarRentalForCorporateCustomerRequest deleteCarRentalForCorporateCustomerRequest) throws BusinessException 
	{
		checkIfCarRentalExistsById(deleteCarRentalForCorporateCustomerRequest.getCarRentalId());
		
		CarRental carRental = this.modelMapperService.forRequest().map(deleteCarRentalForCorporateCustomerRequest, CarRental.class);
		this.carRentalDao.deleteById(carRental.getCarRentalId());
		
		return new SuccessResult("Car Rental Deleted Successfully");
	}

	@Override
	public DataResult<List<CarRentalListDto>> getByCarId(int id) throws BusinessException 
	{
		checkIfCarExistsById(id);
		
		List<CarRental> result = this.carRentalDao.getAllByCar_CarId(id);
		List<CarRentalListDto> response = result.stream()
				.map(carRental -> this.modelMapperService.forDto().map(carRental, CarRentalListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CarRentalListDto>>(response, "Car Rental Listed Successfully");
	}

	@Override
	public DataResult<Boolean> IsACarAvailableOnTheSpecifiedDate(int carId, LocalDate returnDate)
			throws BusinessException 
	{
		checkIfCarExistsById(carId);
		
		var result = this.carRentalDao.getRentalInformationOfTheCarOnTheSpecifiedDate(returnDate, returnDate,
				carId);

		if (result.size() == 0) 
		{
			return new SuccessDataResult<Boolean>(true, "Car can be rented");
		}
		throw new BusinessException("The vehicle was rented on the specified date");
	}

	@Override
	public DataResult<Double> calculatePriceForCorporateCustomer(CreateCarRentalForCorporateCustomerRequest createCarRentalForCorporateCustomerRequest) throws BusinessException
	{
		var result = calculatePrice(createCarRentalForCorporateCustomerRequest.getCarId(), createCarRentalForCorporateCustomerRequest.getStartDate(), createCarRentalForCorporateCustomerRequest.getReturnDate(),
		createCarRentalForCorporateCustomerRequest.getAdditionalServiceIds(),
		createCarRentalForCorporateCustomerRequest.getStartCity(),
		createCarRentalForCorporateCustomerRequest.getEndCity());

		return new SuccessDataResult<Double>(result);
	}

	@Override
	public DataResult<Double> calculatePriceForIndividaulCustomer(CreateCarRentalForIndividualCustomerRequest createCarRentalForIndividualCustomerRequest) throws BusinessException
	{
		var result = calculatePrice(createCarRentalForIndividualCustomerRequest.getCarId(), createCarRentalForIndividualCustomerRequest.getStartDate(), createCarRentalForIndividualCustomerRequest.getReturnDate(),
		createCarRentalForIndividualCustomerRequest.getAdditionalServiceIds(),
		createCarRentalForIndividualCustomerRequest.getStartCity(),
		createCarRentalForIndividualCustomerRequest.getEndCity());

		return new SuccessDataResult<Double>(result);
	}

	@Override
	public DataResult<CarRentalDto> getById(int id) 
	{
		CarRental carRental = this.carRentalDao.getById(id);
		CarRentalDto carRentalDto = this.modelMapperService.forDto().map(carRental,CarRentalDto.class);
		
		return new SuccessDataResult<CarRentalDto>(carRentalDto,"Car Rental Getted Succesfully");
	}

	private double calculatePrice(int carId, LocalDate startDate, LocalDate returnDate, List<Integer> additionalServiceIds, String startCity, String endCity) throws BusinessException{
		
		CarListDto car = this.carService.getById(carId).getData();

		long days = findTheNumberOfDaysToRent(startDate, returnDate);

		double price = days * car.getCarDailyPrice() + calculateExtraPriceByCityDistance(startCity, endCity);

		price += additionalServiceService.calculateAdditionalServicePrice(days, additionalServiceIds).getData();
		return price;
	}
	
	private void insertAddtionalServices(List<Integer> AddtionalServices , int carRentalId) throws BusinessException 
	{
		for(var item : AddtionalServices) 
		{
			CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest= new CreateOrderedAdditionalServiceRequest();
			createOrderedAdditionalServiceRequest.setAdditionalServiceId(item);
			createOrderedAdditionalServiceRequest.setCarRentalId(carRentalId);
			this.orderedAdditionalServiceService.add(createOrderedAdditionalServiceRequest);
		}
	}

	private boolean checkIfItCanBeRented(LocalDate startDate, LocalDate returnDate, int carId) throws BusinessException 
	{
		var result = this.carRentalDao.getRentalInformationOfTheCarOnTheSpecifiedDate(
			startDate, returnDate, carId);
		if (result.size() > 0) 
		{
			throw new BusinessException("Rented between the dates specified in the vehicle");
		}
		return true;
	}

	private boolean checkIfItCanBeRented(LocalDate startDate, LocalDate returnDate, int carId,int carRentalId) throws BusinessException 
	{
		var result = this.carRentalDao.getRentalInformationOfTheCarOnTheSpecifiedDate(
			startDate, returnDate, carId,carRentalId);
		if (result.size() > 0) 
		{
			throw new BusinessException("Rented between the dates specified in the vehicle");
		}
		return true;
	}

	private long findTheNumberOfDaysToRent(LocalDate startDate, LocalDate returnDate) throws BusinessException 
	{
		long days = ChronoUnit.DAYS.between(startDate, returnDate);
		if (days > 0)
			return days;
		throw new BusinessException("Return date cannot be earlier than start date!");
	}

	private Result checkIfCarMaintenance(int carId) throws BusinessException 
	{
		var result = this.carMaintenanceService.getByCar_CarIdAndReturnDate(carId, null);

		if (result.getData() == null) {

			return new SuccessResult("rented");
		}
		throw new BusinessException("in car maintenance");
	}

	private double calculateExtraPriceByCityDistance(String startCity, String endCity) 
	{
		if (startCity.equals(endCity)) 
		{
			return 0;
		}
		return 750;
	}
	
	private void checkIfCarExistsById(int carId) throws BusinessException 
	{
		this.carService.getById(carId);
	}
	
	private void checkIfCarRentalExistsById(int carRentalId) throws BusinessException 
	{
		this.carRentalDao.getById(carRentalId);
	}

	private void insertInvoice(int customerId, Double price, CarRental carRental) throws BusinessException 
	{
		CreateInvoiceRequest createInvoiceRequest= new CreateInvoiceRequest();
		createInvoiceRequest.setCarRentalId(carRental.getCarRentalId());
		createInvoiceRequest.setCreationDate(LocalDate.now());
		createInvoiceRequest.setTotalPrice(price);
		createInvoiceRequest.setCustomerId(customerId);
		this.invoiceService.add(createInvoiceRequest);
	}
}