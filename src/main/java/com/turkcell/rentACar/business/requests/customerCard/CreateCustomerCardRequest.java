package com.turkcell.rentACar.business.requests.customerCard;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerCardRequest
{
    @NotNull
    private String cardOwnerName;

    @NotNull
    private String cardNumber;

    @NotNull
    private String cardCVC;

    @NotNull
    private String cardDate;
    
    @Positive
    @JsonProperty(value = "customerId")
    private int userId;  
}

