package com.turkcell.rentACar.business.requests.orderedAdditionalService;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteOrderedAdditionalServiceRequest 
{
    @Positive
    private int orderedAdditionalServiceId;
}
