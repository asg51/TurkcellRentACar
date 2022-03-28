package com.turkcell.rentACar.business.requests.car;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateCarRequest 
{
    @Min(50)
    @Max(500)
    private double carDailyPrice;

    @Min(2000)
    @Max(2021)
    private int carModelYear;

    @Size(min=20,max=200)
    private String carDescription;

    @PositiveOrZero
    private double kilometerInformation;

    @Positive
    private int colorId;

    @Positive
    private int brandId;
}
