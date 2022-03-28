package com.turkcell.rentACar.business.requests.car;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarKilometerInformationRequest 
{
    @Positive
    private int carId;

    @Positive
    private double newKilometer;
}
