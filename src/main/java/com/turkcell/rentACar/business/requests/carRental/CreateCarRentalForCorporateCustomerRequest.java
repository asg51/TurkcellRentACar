package com.turkcell.rentACar.business.requests.carRental;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarRentalForCorporateCustomerRequest 
{    
    @Positive
    private int carId;
    
    @Positive
    private int corporateCustomerId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate returnDate;

    @Positive
    private int startCityId;

    @Positive
    private int endCityId;

    @PositiveOrZero
    private double startingKilometer;

    @PositiveOrZero
    private double returnKilometer;
}