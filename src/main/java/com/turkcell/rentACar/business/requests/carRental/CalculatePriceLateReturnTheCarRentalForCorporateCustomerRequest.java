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
public class CalculatePriceLateReturnTheCarRentalForCorporateCustomerRequest 
{
    @Positive
    private int carId;

    @NotNull
    private LocalDate expectedDate;

    @NotNull
    private LocalDate returnDate;
}
