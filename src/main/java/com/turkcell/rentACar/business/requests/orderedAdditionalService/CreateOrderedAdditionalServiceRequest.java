package com.turkcell.rentACar.business.requests.orderedAdditionalService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderedAdditionalServiceRequest 
{
    @Positive
    private int carRentalId;

    @Positive
    private int additionalServiceId;
}