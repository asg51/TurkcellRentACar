package com.turkcell.rentACar.api.controllers.singletonTransactions;

import java.util.List;

import com.turkcell.rentACar.business.abstracts.singletonTransactions.CustomerCardService;
import com.turkcell.rentACar.business.dtos.customerCard.CustomerCardDto;
import com.turkcell.rentACar.business.dtos.customerCard.CustomerCardListDto;
import com.turkcell.rentACar.business.requests.customerCard.CreateCustomerCardRequest;
import com.turkcell.rentACar.business.requests.customerCard.DeleteCustomerCardRequest;
import com.turkcell.rentACar.business.requests.customerCard.UpdateCustomerCardRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customerCards")
public class CustomerCardsController {
   
    private CustomerCardService customerCardService;

    public CustomerCardsController(CustomerCardService customerCardService) 
    {
        this.customerCardService = customerCardService;
    }

    @GetMapping("/getAll")
    public DataResult<List<CustomerCardListDto>> getAll()
    {
        return this.customerCardService.getAll();
    }

    @GetMapping("/getById")
    public DataResult<CustomerCardDto> getById(@RequestParam int id) throws BusinessException
    {
    	return this.customerCardService.getById(id);
    }

    @PostMapping("/add")
    public Result add(@RequestBody CreateCustomerCardRequest createCustomerCardRequest) throws BusinessException 
    {
        return this.customerCardService.add(createCustomerCardRequest);
    }
    
    @PutMapping("/update")
    public Result update(@RequestBody UpdateCustomerCardRequest updateCustomerCardRequest) throws BusinessException
    {
        return this.customerCardService.update(updateCustomerCardRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody DeleteCustomerCardRequest deleteCustomerCardRequest) throws BusinessException
    {
        return this.customerCardService.delete(deleteCustomerCardRequest);
    } 
}
