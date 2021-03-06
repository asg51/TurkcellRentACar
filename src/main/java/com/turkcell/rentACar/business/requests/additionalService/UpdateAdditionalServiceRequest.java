package com.turkcell.rentACar.business.requests.additionalService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAdditionalServiceRequest 
{
    @Positive
    private int additionalServiceId;

    @NotNull
    @Size(min = 2, max = 100)
    private String additionalServiceName;

    @Min(0)
    private double dailyPrice;    
}
