package com.turkcell.rentACar.api.controllers;

import com.turkcell.rentACar.business.abstracts.PaymentService;
import com.turkcell.rentACar.business.requests.creates.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.requests.creates.CreatePaymentRequest;
import com.turkcell.rentACar.core.utilities.bankServices.BankService;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class PaymentsController 
{
    private PaymentService paymentService;

    public PaymentsController(PaymentService paymentService) 
    {
        super();
        this.paymentService= paymentService;
    }

    @PostMapping("/add")
    Result add(@RequestBody CreatePaymentRequest  createPaymentRequest) throws BusinessException
    {
    	return paymentService.addForIsBank(createPaymentRequest);
    }
}
