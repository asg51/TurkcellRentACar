package com.turkcell.rentACar.business.requests.color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteColorRequest 
{
    @Positive
    private int colorId;
}
