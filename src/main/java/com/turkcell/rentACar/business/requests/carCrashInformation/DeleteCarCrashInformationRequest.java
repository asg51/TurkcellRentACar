package com.turkcell.rentACar.business.requests.carCrashInformation;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCarCrashInformationRequest 
{
    @Positive
    private int carCrashInformationId;
}