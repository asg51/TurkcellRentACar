package com.turkcell.rentACar.business.requests.carRental;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCarRentalForIndividualCustomerRequest 
{
    @Positive
    private int carRentalId;
}
