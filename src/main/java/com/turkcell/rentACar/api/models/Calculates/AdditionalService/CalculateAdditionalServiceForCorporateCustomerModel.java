package com.turkcell.rentACar.api.models.Calculates.AdditionalService;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
public class CalculateAdditionalServiceForCorporateCustomerModel 
{
    @NotNull
    private List<Integer> orderedAdditionalServiceIds;

    @NotNull
    private LocalDate startDate;
    
    @NotNull
    private LocalDate returnDate;
}
