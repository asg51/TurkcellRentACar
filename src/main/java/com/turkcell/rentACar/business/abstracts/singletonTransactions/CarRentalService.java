package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import java.time.LocalDate;
import java.util.List;

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
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface CarRentalService 
{
    DataResult<List<CarRentalListDto>> getAll();
    DataResult<List<CarRentalListDto>> getByCarId(int id) throws BusinessException;
    DataResult<CarRentalDto> getById(int id);

    DataResult<CarRentalDto> rentForIndividualCustomer(CreateCarRentalForIndividualCustomerRequest createCarRentalForIndividualCustomerRequest) throws BusinessException;
    DataResult<CarRentalDto> rentForCorporateCustomer(CreateCarRentalForCorporateCustomerRequest createCarRentalForCorporateCustomerRequest) throws BusinessException;

    DataResult<CarRentalDto> returnTheRentalCarForCorporateCustomer(ReturnTheRentalCarForCorporateCustomerRequest returnTheRentalCarForCorporateCustomerRequest) throws BusinessException;
    DataResult<CarRentalDto> returnTheRentalCarForIndividualCustomer(ReturnTheRentalCarForIndividualCustomerRequest returnTheRentalCarForIndividualCustomerRequest) throws BusinessException;
    
    DataResult<CarRentalDto> lateReturnTheRentalCarForCorporateCustomer(LateReturnTheRentalCarForCorporateCustomerRequest lateReturnTheRentalCarForCorporateCustomerRequest) throws BusinessException;
    DataResult<CarRentalDto> lateReturnTheRentalCarForIndividualCustomer(LateReturnTheRentalCarForIndividualCustomerRequest lateReturnTheRentalCarForIndividualCustomerRequest) throws BusinessException;

    Result updateForCorporateCustomer(UpdateCarRentalForCorporateCustomerRequest updateCarRentalForCorporateCustomerRequest) throws BusinessException;
    Result updateForIndividualCustomer(UpdateCarRentalForIndividualCustomerRequest updateCarRentalForIndividualCustomerRequest) throws BusinessException;

    Result deleteForIndividualCustomer(DeleteCarRentalForIndividualCustomerRequest deleteCarRentalForIndividualCustomerRequest) throws BusinessException;
    Result deleteForCorporateCustomer(DeleteCarRentalForCorporateCustomerRequest deleteCarRentalForCorporateCustomerRequest) throws BusinessException;
    
    Result IsACarAvailableOnTheSpecifiedDate(int carId,LocalDate returnDate) throws BusinessException;

    DataResult<Double> calculatePriceCarRentalForIndividualCustomer(CalculatePriceCarRentalForIndividualCustomerRequest calculateCarRentalForIndividualCustomerModel) throws BusinessException;
    DataResult<Double> calculatePriceCarRentalForCorporateCustomer(CalculatePriceCarRentalForCorporateCustomerRequest calculateCarRentalForCorporateCustomerModel) throws BusinessException;
    
    DataResult<Double> calculatePriceLateReturnTheCarRentalForIndividualCustomer(CalculatePriceLateReturnTheCarRentalForIndividualCustomerRequest calculatePriceLateReturnTheCarRentalForIndividualCustomerRequest) throws BusinessException;
    DataResult<Double> calculatePriceLateReturnTheCarRentalForCorporateCustomer(CalculatePriceLateReturnTheCarRentalForCorporateCustomerRequest calculatePriceLateReturnTheCarRentalForCorporateCustomerRequest) throws BusinessException;

    Result checkIfCarRentalExistsById(int carRentalId) throws BusinessException;
}