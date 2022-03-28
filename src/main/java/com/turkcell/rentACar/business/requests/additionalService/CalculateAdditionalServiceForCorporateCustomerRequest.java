package com.turkcell.rentACar.business.requests.additionalService;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
public class CalculateAdditionalServiceForCorporateCustomerRequest 
{
    @NotNull
    private List<Integer> orderedAdditionalServiceIds;

    @NotNull
    private LocalDate startDate;
    
    @NotNull
    private LocalDate returnDate;
}
