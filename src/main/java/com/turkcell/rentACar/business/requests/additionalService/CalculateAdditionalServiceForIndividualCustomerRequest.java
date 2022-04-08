package com.turkcell.rentACar.business.requests.additionalService;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateAdditionalServiceForIndividualCustomerRequest
{
    @NotNull
    private List<Integer> additionalServiceIds;

    @NotNull
    private LocalDate startDate;
    
    @NotNull
    private LocalDate returnDate;
}
