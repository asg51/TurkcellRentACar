package com.turkcell.rentACar.business.requests.payment;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletePaymentRequest 
{
    @Positive
    private int paymentId;
}
