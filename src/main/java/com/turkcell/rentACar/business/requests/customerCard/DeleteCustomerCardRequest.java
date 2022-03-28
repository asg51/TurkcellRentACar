package com.turkcell.rentACar.business.requests.customerCard;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCustomerCardRequest 
{
    @Positive
    private int customerCardId;   
}
