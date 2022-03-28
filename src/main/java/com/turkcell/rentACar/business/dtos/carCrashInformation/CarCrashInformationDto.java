package com.turkcell.rentACar.business.dtos.carCrashInformation;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarCrashInformationDto 
{
    private int carCrashInformationId;
    private String crashDetail;
    private int carId;
}