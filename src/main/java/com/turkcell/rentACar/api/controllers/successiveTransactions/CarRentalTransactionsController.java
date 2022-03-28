package com.turkcell.rentACar.api.controllers.successiveTransactions;

import com.turkcell.rentACar.api.models.CarRentalReturnProcessInformationForCorporateModel;
import com.turkcell.rentACar.api.models.CarRentalReturnProcessInformationForIndividualModel;
import com.turkcell.rentACar.api.models.CarRentalTransactionInformationForCorporateCustomerModel;
import com.turkcell.rentACar.api.models.CarRentalTransactionInformationForIndividualCustomerModel;
import com.turkcell.rentACar.api.models.ReturnLateArrivedRentalCarForCorporateCustomerModel;
import com.turkcell.rentACar.api.models.ReturnLateArrivedRentalCarForIndivualCustomerModel;
import com.turkcell.rentACar.business.abstracts.successiveTransactions.CarRentalTransactionsService;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/CarRentalTransactions")
public class CarRentalTransactionsController 
{
    private CarRentalTransactionsService carRentalTransactionsService;

    @Autowired
    public CarRentalTransactionsController(CarRentalTransactionsService carRentalTransactionsService) 
    {
        this.carRentalTransactionsService = carRentalTransactionsService;
    }

    @PostMapping("/makeACarRentalForCorporateCustomer")
    public Result makeACarRentalForCorporateCustomer(@RequestBody CarRentalTransactionInformationForCorporateCustomerModel carRentalTransactionInformationForCorporateCustomerModel) throws BusinessException
    {
        return carRentalTransactionsService.makeACarRentalForCorporateCustomer(carRentalTransactionInformationForCorporateCustomerModel);
    }

    @PostMapping("/makeACarRentalForIndividualCustomer")
    public Result makeACarRentalForIndividualCustomer(@RequestBody CarRentalTransactionInformationForIndividualCustomerModel carRentalTransactionInformationForIndividualCustomerModel) throws BusinessException
    {
        return carRentalTransactionsService.makeACarRentalForIndivualCustomer(carRentalTransactionInformationForIndividualCustomerModel);
    }

    @PostMapping("/returnTheRentalCarForCorporateCustomer")
    Result returnTheRentalCarForCorporateCustomer(@RequestBody CarRentalReturnProcessInformationForCorporateModel carRentalReturnProcessInformationForCorporateModel) throws BusinessException
    {
        return carRentalTransactionsService.returnTheRentalCarForCorporateCustomer(carRentalReturnProcessInformationForCorporateModel);
    }

    @PostMapping("/returnTheRentalCarForIndividualCustomer")
    Result returnTheRentalCarForIndivualCustomer(@RequestBody CarRentalReturnProcessInformationForIndividualModel carRentalReturnProcessInformationForIndividualModel) throws BusinessException
    {
        return carRentalTransactionsService.returnTheRentalCarForIndividualCustomer(carRentalReturnProcessInformationForIndividualModel);
    }
    
    @PostMapping("/returnLateArrivedRentalCarForCorporateCustomer")
    Result returnLateArrivedRentalCarForCorporateCustomer(@RequestBody ReturnLateArrivedRentalCarForCorporateCustomerModel returnTheBeRentalCarForCorporateCustomerModel) throws BusinessException
    {
        return carRentalTransactionsService.returnLateArrivedRentalCarForCorporateCustomer(returnTheBeRentalCarForCorporateCustomerModel);
    }

    @PostMapping("/returnLateArrivedRentalCarForIndivualCustomer")
    Result returnLateArrivedRentalCarForIndivualCustomer(@RequestBody ReturnLateArrivedRentalCarForIndivualCustomerModel returnTheBeRentalCarForIndividualCustomerModel) throws BusinessException
    {
        return carRentalTransactionsService.returnLateArrivedRentalCarForIndivualCustomer(returnTheBeRentalCarForIndividualCustomerModel);
    }
}
