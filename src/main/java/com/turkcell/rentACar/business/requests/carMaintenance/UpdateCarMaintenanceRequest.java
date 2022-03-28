package com.turkcell.rentACar.business.requests.carMaintenance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarMaintenanceRequest 
{
    @Positive
    private int maintenanceId;

    @Size(min=2,max=50)
    @NotNull
    @NotBlank
    private String maintenanceDescription;

    @Positive
    private int carId;

    @NotNull
    private LocalDate returnDate;
}
