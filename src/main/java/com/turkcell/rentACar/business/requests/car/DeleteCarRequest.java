package com.turkcell.rentACar.business.requests.car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCarRequest 
{
    @Positive
    private int carId;
}

