package com.turkcell.rentACar.business.requests.invoice;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInvoiceRequest 
{
    @Positive
    private int invoiceId;

    @NotNull
    private LocalDate creationDate;

    @Min(0)
    private double totalPrice;

    @Positive
    private int carRentalId;

    @Positive
    private int customerId;
}