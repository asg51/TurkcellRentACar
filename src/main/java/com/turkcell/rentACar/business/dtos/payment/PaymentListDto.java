package com.turkcell.rentACar.business.dtos.payment;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentListDto 
{
    private int paymentId;
    private double price;
    private LocalDate paymentDate;
        
    @JsonProperty(value = "email")
    private String customer_Email;

    private int invoiceId;
}
