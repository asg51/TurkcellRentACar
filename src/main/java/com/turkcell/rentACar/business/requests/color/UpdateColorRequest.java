package com.turkcell.rentACar.business.requests.color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateColorRequest 
{
    @Positive
    private int colorId;

    @NotNull
    @NotBlank
    @Size(min=2,max=15)
    private String colorName;
}
