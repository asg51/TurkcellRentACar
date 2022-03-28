package com.turkcell.rentACar.business.requests.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DeleteBrandRequest 
{
    @Positive
    private int brandId;
}
