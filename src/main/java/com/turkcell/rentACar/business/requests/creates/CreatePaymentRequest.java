package com.turkcell.rentACar.business.requests.creates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest 
{
    
    private int customerId;
    private String cardOwnerName;
    private String cardNumber;
    private String cardCVC;
    private String card;
    private double price;
}
