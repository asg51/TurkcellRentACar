package com.turkcell.rentACar.business.requests.additionalService;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAdditionalServiceRequest
{
    @Positive
    private int orderedAdditionalServiceId;
}