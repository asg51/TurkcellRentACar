package com.turkcell.rentACar.business.dtos.car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto 
{
    private double carDailyPrice;
    private int carModelYear;
    private String carDescription;
    private double kilometerInformation;
    private String brandName;
    private String colorName;
}
