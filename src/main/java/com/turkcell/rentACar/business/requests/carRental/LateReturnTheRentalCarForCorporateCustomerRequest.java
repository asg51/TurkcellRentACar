package com.turkcell.rentACar.business.requests.carRental;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LateReturnTheRentalCarForCorporateCustomerRequest 
{
    @Positive
    private int carRentalId;

    @Positive
    private int corporateCustomerId;

    @NotNull
    private LocalDate returnDate;
    
    @Positive
    private double returnKilometer;
}
