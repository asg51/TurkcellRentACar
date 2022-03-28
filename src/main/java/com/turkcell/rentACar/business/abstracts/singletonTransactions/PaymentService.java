package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import java.util.List;

import com.turkcell.rentACar.business.dtos.payment.PaymentDto;
import com.turkcell.rentACar.business.dtos.payment.PaymentListDto;
import com.turkcell.rentACar.business.requests.payment.CreatePaymentRequest;
import com.turkcell.rentACar.business.requests.payment.DeletePaymentRequest;
import com.turkcell.rentACar.business.requests.payment.UpdatePaymentRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface PaymentService 
{
    DataResult<List<PaymentListDto>> getAll();
    Result add(CreatePaymentRequest createPaymentRequest) throws BusinessException;
    Result update(UpdatePaymentRequest updatePaymentRequest) throws BusinessException;
    Result delete(DeletePaymentRequest deletePaymentRequest) throws BusinessException;
    DataResult<PaymentDto> getById(int id) throws BusinessException;
}
