package com.turkcell.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import com.turkcell.rentACar.business.abstracts.PaymentService;
import com.turkcell.rentACar.business.adapters.bankAdapters.IsBankService;
import com.turkcell.rentACar.business.adapters.bankAdapters.ZiraatBankService;
import com.turkcell.rentACar.business.dtos.PaymentListDto;
import com.turkcell.rentACar.business.requests.creates.CreatePaymentRequest;
import com.turkcell.rentACar.business.requests.deletes.DeletePaymentRequest;
import com.turkcell.rentACar.business.requests.updates.UpdatePaymentRequest;
import com.turkcell.rentACar.core.utilities.bankServices.BankService;
import com.turkcell.rentACar.core.utilities.bankServices.ZiraatBankManager;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.PaymentDao;
import com.turkcell.rentACar.entities.concretes.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import aj.org.objectweb.asm.Type;

@Service
public class PaymentManager implements PaymentService
{ 
    private PaymentDao paymentDao;
    private ModelMapperService modelMapperService;
    private ZiraatBankService ziraatBankService;
    private IsBankService isBankService;

    @Autowired
    public PaymentManager(PaymentDao paymentDao, ModelMapperService modelMapperService,ZiraatBankService ziraatBankService,IsBankService isBankService) 
    {
        super();
        this.paymentDao = paymentDao;
        this.modelMapperService = modelMapperService;
        this.isBankService= isBankService;
        this.ziraatBankService= ziraatBankService;
    }

    @Override
    public DataResult<List<PaymentListDto>> getAll() 
    {
        List<Payment> result = this.paymentDao.findAll();
		List<PaymentListDto> response = result.stream().map(payment->this.modelMapperService.forDto().map(payment,PaymentListDto.class)).collect(Collectors.toList());
		
		return new SuccessDataResult<List<PaymentListDto>>(response,"Cars Listed succesfully");
    }

    @Override
    public Result addForZirratBank(CreatePaymentRequest createPaymentRequest) throws BusinessException 
    {
        this.ziraatBankService.addPayments(createPaymentRequest.getCardOwnerName(), createPaymentRequest.getCardNumber(), createPaymentRequest.getCardCVC(), createPaymentRequest.getCard(), createPaymentRequest.getPrice());

        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest,Payment.class);
		this.paymentDao.save(payment);
		
		return new SuccessResult("Payment Added Successfully");
    }

    @Override
    public Result addForIsBank(CreatePaymentRequest createPaymentRequest) throws BusinessException 
    {
        this.isBankService.addPayments(createPaymentRequest.getCardOwnerName(), createPaymentRequest.getCardNumber(), createPaymentRequest.getCardCVC(), createPaymentRequest.getCard(), createPaymentRequest.getPrice());

        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest,Payment.class);
		this.paymentDao.save(payment);
		
		return new SuccessResult("Payment Added Successfully");
    }

    @Override
    public Result update(UpdatePaymentRequest updatePaymentRequest) throws BusinessException 
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result delete(DeletePaymentRequest deletePaymentRequest) throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DataResult<PaymentListDto> getById(int id) throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
