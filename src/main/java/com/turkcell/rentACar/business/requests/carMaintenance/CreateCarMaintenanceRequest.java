package com.turkcell.rentACar.business.requests.carMaintenance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarMaintenanceRequest 
{
    @Size(min=2,max=50)
    @NotNull
    @NotBlank
    private String maintenanceDescription;

    @Positive
    private int carId;
    
    @NotNull
    private LocalDate returnDate;

}
