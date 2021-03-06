package com.turkcell.rentACar.business.concretes.singletonTransactions;

import com.turkcell.rentACar.business.abstracts.singletonTransactions.InvoiceService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.invoice.InvoiceDto;
import com.turkcell.rentACar.business.dtos.invoice.InvoiceListDto;
import com.turkcell.rentACar.business.requests.invoice.CreateInvoiceRequest;
import com.turkcell.rentACar.business.requests.invoice.DeleteInvoiceRequest;
import com.turkcell.rentACar.business.requests.invoice.UpdateInvoiceRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.InvoiceDao;
import com.turkcell.rentACar.entities.concretes.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceManager implements InvoiceService 
{
	private InvoiceDao invoiceDao;
	private ModelMapperService modelMapperService;

	@Autowired
	public InvoiceManager(InvoiceDao invoiceDao, ModelMapperService modelMapperService) 
    {
		this.invoiceDao = invoiceDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<InvoiceListDto>> getAll() 
    {
		List<Invoice> result = this.invoiceDao.findAll();
		List<InvoiceListDto> response = result.stream()
				.map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<InvoiceListDto>>(response, BusinessMessages.INVOICE_LISTED);
	}

	@Override
	public DataResult<InvoiceDto> add(CreateInvoiceRequest createInvoiceRequest) throws BusinessException 
    {
		Invoice invoice = this.modelMapperService.forRequest().map(createInvoiceRequest, Invoice.class);
		invoice.setInvoiceId(0);
		invoice.getCustomer().setUserId(createInvoiceRequest.getCustomerId());

		invoice = this.invoiceDao.save(invoice);

		InvoiceDto invoiceDto = this.modelMapperService.forDto().map(invoice, InvoiceDto.class);

		return new SuccessDataResult<InvoiceDto>(invoiceDto, BusinessMessages.INVOICE_ADDED);
	}

	@Override
	public DataResult<InvoiceDto> getById(int id) throws BusinessException 
    {
		checkIfExistByInvoiceId(id);

		Invoice invoice = invoiceDao.getById(id);
		InvoiceDto invoiceDto = this.modelMapperService.forDto().map(invoice, InvoiceDto.class);

		return new SuccessDataResult<InvoiceDto>(invoiceDto, BusinessMessages.INVOICE_GETTED);
	}

	@Override
	public Result update(UpdateInvoiceRequest updateInvoiceRequest) throws BusinessException 
    {
		Invoice invoice = this.invoiceDao.getByCarRental_CarRentalId(updateInvoiceRequest.getCarRentalId());

		checkIfExistByInvoiceId(invoice.getInvoiceId());
		
		Invoice invoiceUpdate = this.modelMapperService.forRequest().map(updateInvoiceRequest, Invoice.class);
		invoiceUpdate.setInvoiceId(invoice.getInvoiceId());

		this.invoiceDao.save(invoiceUpdate);

		return new SuccessResult(BusinessMessages.INVOICE_UPDATED);
	}

	@Override
	public Result delete(DeleteInvoiceRequest deleteInvoiceRequest) throws BusinessException 
    {
		checkIfExistByInvoiceId(deleteInvoiceRequest.getInvoiceId());

		Invoice invoice = this.modelMapperService.forRequest().map(deleteInvoiceRequest, Invoice.class);
		this.invoiceDao.delete(invoice);

		return new SuccessResult(BusinessMessages.INVOICE_DELETED);
	}

	@Override
	public Result checkIfExistByInvoiceId(int invoiceId) throws BusinessException 
    {
		if (!this.invoiceDao.existsById(invoiceId)) 
		{
			throw new BusinessException(BusinessMessages.INVOICE_NOT_FOUND);
		}

		return new SuccessResult(BusinessMessages.INVOICE_FOUND);
	}
}