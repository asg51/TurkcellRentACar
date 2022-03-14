package com.turkcell.rentACar.business.requests.updates;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarRentalForIndividualCustomerRequest 
{
    @NotNull
    @Positive
    private int carRentalId;

    @NotNull
    @Positive
    private int carId;
    
    @Positive
    private int individualCustomerId;

    @NotNull
    private List<Integer> additionalServiceIds;

    @NotNull
    private LocalDate startDate;
    
    @NotNull
    private LocalDate returnDate;
    
    @NotNull
    @NotBlank
    private String startCity;
    
    @NotNull
    @NotBlank
    private String endCity;
}