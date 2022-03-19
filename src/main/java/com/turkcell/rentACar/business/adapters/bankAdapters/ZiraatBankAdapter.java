package com.turkcell.rentACar.business.adapters.bankAdapters;

import com.turkcell.rentACar.core.utilities.bankServices.ZiraatBankManager;

public class ZiraatBankAdapter implements ZiraatBankService
{
    @Override
    public boolean addPayments(String cardOwnerName, String cardNumber, String cardCVC, String card, double price) 
    {
        ZiraatBankManager bankManager=new ZiraatBankManager();
        return bankManager.addPayments(cardNumber, card, price, cardOwnerName, cardCVC);
    }
}
