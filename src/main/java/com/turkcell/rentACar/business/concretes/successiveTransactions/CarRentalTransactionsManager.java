package com.turkcell.rentACar.business.concretes.successiveTransactions;

import java.time.LocalDate;
import java.util.List;

import com.turkcell.rentACar.api.models.CarRentalReturnProcessInformationForCorporateModel;
import com.turkcell.rentACar.api.models.CarRentalReturnProcessInformationForIndividualModel;
import com.turkcell.rentACar.api.models.CarRentalTransactionInformationForCorporateCustomerModel;
import com.turkcell.rentACar.api.models.CarRentalTransactionInformationForIndividualCustomerModel;
import com.turkcell.rentACar.api.models.ReturnLateArrivedRentalCarForCorporateCustomerModel;
import com.turkcell.rentACar.api.models.ReturnLateArrivedRentalCarForIndivualCustomerModel;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.AdditionalServiceService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.CarRentalService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.CarService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.InvoiceService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.OrderedAdditionalServiceService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.PaymentService;
import com.turkcell.rentACar.business.abstracts.singletonTransactions.PosService;
import com.turkcell.rentACar.business.abstracts.successiveTransactions.CarRentalTransactionsService;
import com.turkcell.rentACar.business.dtos.carRental.CarRentalDto;
import com.turkcell.rentACar.business.dtos.invoice.InvoiceDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalService.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.additionalService.CalculateAdditionalServiceForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.additionalService.CalculateAdditionalServiceForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.car.UpdateCarKilometerInformationRequest;
import com.turkcell.rentACar.business.requests.carRental.CalculatePriceCarRentalForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.CalculatePriceCarRentalForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.CalculatePriceLateReturnTheCarRentalForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.CalculatePriceLateReturnTheCarRentalForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.CreateCarRentalForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.CreateCarRentalForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.LateReturnTheRentalCarForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.LateReturnTheRentalCarForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.ReturnTheRentalCarForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.carRental.ReturnTheRentalCarForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.invoice.CreateInvoiceRequest;
import com.turkcell.rentACar.business.requests.payment.CreatePaymentRequest;
import com.turkcell.rentACar.core.utilities.bankServices.BankInformation;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarRentalTransactionsManager implements CarRentalTransactionsService
{
    private ModelMapperService modelMapperService;

    private PosService posService;

    private CarService carService;
    private CarRentalService carRentalService;
    private OrderedAdditionalServiceService orderedAdditionalServiceService;
    private AdditionalServiceService additionalServiceService;
    private InvoiceService invoiceService;
    private PaymentService paymentService;

    public CarRentalTransactionsManager(ModelMapperService modelMapperService, CarService carService, CarRentalService carRentalService, PosService posService,
    OrderedAdditionalServiceService orderedAdditionalServiceService,
    AdditionalServiceService additionalServiceService,InvoiceService invoiceService,PaymentService paymentService) 
    {
        this.modelMapperService = modelMapperService;

        this.posService = posService;

        this.carService = carService;
        this.carRentalService = carRentalService;
        this.orderedAdditionalServiceService = orderedAdditionalServiceService;
        this.additionalServiceService = additionalServiceService;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public Result makeACarRentalForCorporateCustomer(CarRentalTransactionInformationForCorporateCustomerModel carRentalTransactionInformationForCorporateCustomerModel) throws BusinessException 
    {
        CalculatePriceCarRentalForCorporateCustomerRequest calculateCarRentalForCorporateCustomerRequest = this.modelMapperService.forRequest().map(carRentalTransactionInformationForCorporateCustomerModel, CalculatePriceCarRentalForCorporateCustomerRequest.class);
        CalculateAdditionalServiceForCorporateCustomerRequest calculateAdditionalServiceForCorporateCustomerRequest = this.modelMapperService.forRequest().map(carRentalTransactionInformationForCorporateCustomerModel, CalculateAdditionalServiceForCorporateCustomerRequest.class);

        double price = calculateCarRentalTotalPriceForCorporateCustomer(calculateCarRentalForCorporateCustomerRequest, calculateAdditionalServiceForCorporateCustomerRequest);

        makeAMoneyPayment(carRentalTransactionInformationForCorporateCustomerModel.getBankInformation(),price);

        CarRentalDto carRentalDto = insertCarRental(carRentalTransactionInformationForCorporateCustomerModel);

        insertOrderedAdditionalService(carRentalTransactionInformationForCorporateCustomerModel.getAdditionalServiceIds(),carRentalDto.getCarRentalId());

        InvoiceDto invoiceDto = insertInvoice(carRentalDto.getCarRentalId(), carRentalTransactionInformationForCorporateCustomerModel.getCorporateCustomerId(), price);

        insertPayment(carRentalTransactionInformationForCorporateCustomerModel.getCorporateCustomerId(),invoiceDto.getInvoiceId(),price);

        return new SuccessResult();
    }

    @Override
    @Transactional
    public Result makeACarRentalForIndivualCustomer(CarRentalTransactionInformationForIndividualCustomerModel carRentalTransactionInformationForIndiviualCustomerModel) throws BusinessException 
    {
        CalculatePriceCarRentalForIndividualCustomerRequest calculatePriceCarRentalForIndividualCustomerRequest = this.modelMapperService.forRequest().map(carRentalTransactionInformationForIndiviualCustomerModel, CalculatePriceCarRentalForIndividualCustomerRequest.class);
        CalculateAdditionalServiceForIndividualCustomerRequest calculateAdditionalServiceForCorporateCustomerRequest= this.modelMapperService.forRequest().map(carRentalTransactionInformationForIndiviualCustomerModel, CalculateAdditionalServiceForIndividualCustomerRequest.class);

        double price = calculateCarRentalTotalPriceForIndividualCustomer(calculatePriceCarRentalForIndividualCustomerRequest, calculateAdditionalServiceForCorporateCustomerRequest);

        makeAMoneyPayment(carRentalTransactionInformationForIndiviualCustomerModel.getBankInformation(),price);

        CarRentalDto carRentalDto = insertCarRental(carRentalTransactionInformationForIndiviualCustomerModel);

        insertOrderedAdditionalService(carRentalTransactionInformationForIndiviualCustomerModel.getAdditionalServiceIds(),carRentalDto.getCarRentalId());

        InvoiceDto invoiceDto = insertInvoice(carRentalDto.getCarRentalId(), carRentalTransactionInformationForIndiviualCustomerModel.getIndividualCustomerId(), price);

        insertPayment(carRentalTransactionInformationForIndiviualCustomerModel.getIndividualCustomerId(),invoiceDto.getInvoiceId(),price);

        return new SuccessResult();
    }

    @Override
    @Transactional
    public Result returnTheRentalCarForCorporateCustomer(CarRentalReturnProcessInformationForCorporateModel carRentalReturnProcessInformationForCorporateModel)throws BusinessException 
    {
        ReturnTheRentalCarForCorporateCustomerRequest returnTheRentalCarForCorporateCustomerRequest = this.modelMapperService.forRequest().map(carRentalReturnProcessInformationForCorporateModel, ReturnTheRentalCarForCorporateCustomerRequest.class);

        CarRentalDto carRentalDto = returnTheRentalCarForCorporateCustomer(returnTheRentalCarForCorporateCustomerRequest);

        UpdateCarKilometerInformationRequest updateCarKilometerInformationRequest = new UpdateCarKilometerInformationRequest(carRentalDto.getCar_CarId(), carRentalReturnProcessInformationForCorporateModel.getReturnKilometer());
        updateOfKilometresForCar(updateCarKilometerInformationRequest);
    
        return new SuccessResult();
    }

    @Override
    @Transactional
    public Result returnTheRentalCarForIndividualCustomer(CarRentalReturnProcessInformationForIndividualModel carRentalReturnProcessInformationForIndividualModel) throws BusinessException 
    {
        ReturnTheRentalCarForIndividualCustomerRequest returnTheRentalCarForIndivualCustomerRequest = this.modelMapperService.forRequest().map(carRentalReturnProcessInformationForIndividualModel, ReturnTheRentalCarForIndividualCustomerRequest.class);

        CarRentalDto carRentalDto = returnTheRentalCarForIndividualCustomer(returnTheRentalCarForIndivualCustomerRequest);

        UpdateCarKilometerInformationRequest updateCarKilometerInformationRequest = new UpdateCarKilometerInformationRequest(carRentalDto.getCar_CarId(), carRentalReturnProcessInformationForIndividualModel.getReturnKilometer());
        updateOfKilometresForCar(updateCarKilometerInformationRequest);
    
        return new SuccessResult();
    }

    @Override
    @Transactional
    public Result returnLateArrivedRentalCarForCorporateCustomer(ReturnLateArrivedRentalCarForCorporateCustomerModel returnLateArrivedRentalCarForCorporateCustomerModel) throws BusinessException 
    {
        CarRentalDto carRentalDto = this.carRentalService.getById(returnLateArrivedRentalCarForCorporateCustomerModel.getCarRentalId()).getData();

        LateReturnTheRentalCarForCorporateCustomerRequest lateReturnTheRentalCarForCorporateCustomerRequest = this.modelMapperService.forRequest().map(returnLateArrivedRentalCarForCorporateCustomerModel, LateReturnTheRentalCarForCorporateCustomerRequest.class);

        lateReturnTheRentalCarForCorporateCustomer(lateReturnTheRentalCarForCorporateCustomerRequest);

        List<Integer> orderedAdditionalServiceIds = findAdditionalServiceIds(carRentalDto.getCarRentalId());

        CalculatePriceLateReturnTheCarRentalForCorporateCustomerRequest calculatePriceLateReturnTheCarRentalForCorporateCustomerRequest = new CalculatePriceLateReturnTheCarRentalForCorporateCustomerRequest(carRentalDto.getCar_CarId(),
            carRentalDto.getReturnDate(), returnLateArrivedRentalCarForCorporateCustomerModel.getReturnDate());
        CalculateAdditionalServiceForCorporateCustomerRequest calculateAdditionalServiceForCorporateCustomerRequest = new CalculateAdditionalServiceForCorporateCustomerRequest(
            orderedAdditionalServiceIds, carRentalDto.getReturnDate(), returnLateArrivedRentalCarForCorporateCustomerModel.getReturnDate());

        double price = calculateLateReturnTheCarRentalTotalPriceForCorporateCustomer(calculatePriceLateReturnTheCarRentalForCorporateCustomerRequest, calculateAdditionalServiceForCorporateCustomerRequest);

        makeAMoneyPayment(returnLateArrivedRentalCarForCorporateCustomerModel.getBankInformation(),price);

        InvoiceDto invoiceDto = insertInvoice(returnLateArrivedRentalCarForCorporateCustomerModel.getCarRentalId(), returnLateArrivedRentalCarForCorporateCustomerModel.getCorporateCustomerId(), price);
        
        insertPayment(returnLateArrivedRentalCarForCorporateCustomerModel.getCorporateCustomerId(), invoiceDto.getInvoiceId(), price);

        UpdateCarKilometerInformationRequest updateCarKilometerInformationRequest = new UpdateCarKilometerInformationRequest(carRentalDto.getCar_CarId(), returnLateArrivedRentalCarForCorporateCustomerModel.getReturnKilometer());
        updateOfKilometresForCar(updateCarKilometerInformationRequest);

        return new SuccessResult();
    }

    @Override
    @Transactional
    public Result returnLateArrivedRentalCarForIndivualCustomer(ReturnLateArrivedRentalCarForIndivualCustomerModel returnLateArrivedRentalCarForIndivualCustomerModel) throws BusinessException 
    {
        CarRentalDto carRentalDto = this.carRentalService.getById(returnLateArrivedRentalCarForIndivualCustomerModel.getCarRentalId()).getData();

        LateReturnTheRentalCarForIndividualCustomerRequest lateReturnTheRentalCarForIndividualCustomerRequest = this.modelMapperService.forRequest().map(returnLateArrivedRentalCarForIndivualCustomerModel, LateReturnTheRentalCarForIndividualCustomerRequest.class);

        lateReturnTheRentalCarForIndividualCustomer(lateReturnTheRentalCarForIndividualCustomerRequest);

        List<Integer> orderedAdditionalServiceIds = findAdditionalServiceIds(carRentalDto.getCarRentalId());

        CalculatePriceLateReturnTheCarRentalForIndividualCustomerRequest calculatePriceLateReturnTheCarRentalForIndividualCustomerRequest = new CalculatePriceLateReturnTheCarRentalForIndividualCustomerRequest(carRentalDto.getCar_CarId(),
            carRentalDto.getReturnDate(), returnLateArrivedRentalCarForIndivualCustomerModel.getReturnDate());
        CalculateAdditionalServiceForIndividualCustomerRequest calculateAdditionalServiceForIndividualCustomerRequest = new CalculateAdditionalServiceForIndividualCustomerRequest(
            orderedAdditionalServiceIds, carRentalDto.getReturnDate(), returnLateArrivedRentalCarForIndivualCustomerModel.getReturnDate());

        double price = calculateCarRentalTotalPriceForIndividualCustomer(calculatePriceLateReturnTheCarRentalForIndividualCustomerRequest, calculateAdditionalServiceForIndividualCustomerRequest);

        makeAMoneyPayment(returnLateArrivedRentalCarForIndivualCustomerModel.getBankInformation(),price);

        InvoiceDto invoiceDto = insertInvoice(returnLateArrivedRentalCarForIndivualCustomerModel.getCarRentalId(), returnLateArrivedRentalCarForIndivualCustomerModel.getIndividualCustomerId(), price);
        
        insertPayment(returnLateArrivedRentalCarForIndivualCustomerModel.getIndividualCustomerId(), invoiceDto.getInvoiceId(), price);

        UpdateCarKilometerInformationRequest updateCarKilometerInformationRequest = new UpdateCarKilometerInformationRequest(carRentalDto.getCar_CarId(), returnLateArrivedRentalCarForIndivualCustomerModel.getReturnKilometer());
        updateOfKilometresForCar(updateCarKilometerInformationRequest);

        return new SuccessResult();
    }

    private void makeAMoneyPayment(BankInformation bankInformation, double price) throws BusinessException
    {
        this.posService.pay(bankInformation,price);
    }

    private CarRentalDto insertCarRental(CarRentalTransactionInformationForCorporateCustomerModel carRentalTransactionInformationForCorporateCustomerModel)throws BusinessException 
    {
        CreateCarRentalForCorporateCustomerRequest createCarRentalForCorporateCustomerRequest= this.modelMapperService.forRequest()
        .map(carRentalTransactionInformationForCorporateCustomerModel, CreateCarRentalForCorporateCustomerRequest.class);

        double kilometerInformation = this.carService.getById(carRentalTransactionInformationForCorporateCustomerModel.getCarId()).getData().getKilometerInformation();

        createCarRentalForCorporateCustomerRequest.setStartingKilometer(kilometerInformation);
        createCarRentalForCorporateCustomerRequest.setReturnKilometer(kilometerInformation);

        CarRentalDto carRentalDto = this.carRentalService.rentForCorporateCustomer(createCarRentalForCorporateCustomerRequest).getData();

        return carRentalDto;
    }
    
    private CarRentalDto insertCarRental(CarRentalTransactionInformationForIndividualCustomerModel carRentalTransactionInformationForIndiviualCustomerModel)throws BusinessException 
    {
        CreateCarRentalForIndividualCustomerRequest createCarRentalForIndividualCustomerRequest= this.modelMapperService.forRequest()
        .map(carRentalTransactionInformationForIndiviualCustomerModel, CreateCarRentalForIndividualCustomerRequest.class);

        double kilometerInformation = this.carService.getById(createCarRentalForIndividualCustomerRequest.getCarId()).getData().getKilometerInformation();

        createCarRentalForIndividualCustomerRequest.setStartingKilometer(kilometerInformation);
        createCarRentalForIndividualCustomerRequest.setReturnKilometer(kilometerInformation);

        CarRentalDto carRentalDto = this.carRentalService.rentForIndividualCustomer(createCarRentalForIndividualCustomerRequest).getData();

        return carRentalDto;
    }

    private void insertOrderedAdditionalService(List<Integer> additionalServiceIds, int carRentalId) throws BusinessException
    {
        this.orderedAdditionalServiceService.addServices(additionalServiceIds, carRentalId);
    }

    private InvoiceDto insertInvoice(int carRentalId, int customerId,double price) throws BusinessException
    {
        CreateInvoiceRequest createInvoiceRequest = new CreateInvoiceRequest(LocalDate.now(), price, carRentalId, customerId);

        InvoiceDto invoiceDto = this.invoiceService.add(createInvoiceRequest).getData();

        return invoiceDto;
    }

    private void insertPayment(int customerId, int invoiceId, double price) throws BusinessException
    {
        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest(customerId, invoiceId, price);
        this.paymentService.add(createPaymentRequest);
    }

    private double calculateCarRentalTotalPriceForIndividualCustomer(CalculatePriceCarRentalForIndividualCustomerRequest calculatePriceCarRentalForIndividualCustomerRequest, 
        CalculateAdditionalServiceForIndividualCustomerRequest calculateAdditionalServiceForIndividualCustomerRequest) throws BusinessException
    {
        double price = 0.0;

        price = this.carRentalService.calculatePriceCarRentalForIndividualCustomer(calculatePriceCarRentalForIndividualCustomerRequest).getData();

        price += this.additionalServiceService.calculateAdditionalServicePriceForIndividualCustomer(calculateAdditionalServiceForIndividualCustomerRequest).getData();

        return price;
    }
   
    private double calculateCarRentalTotalPriceForCorporateCustomer(CalculatePriceCarRentalForCorporateCustomerRequest calculatePriceCarRentalForCorporateCustomerRequest, 
        CalculateAdditionalServiceForCorporateCustomerRequest calculateAdditionalServiceForCorporateCustomerRequest) throws BusinessException
    {
        double price = 0.0;

        price = this.carRentalService.calculatePriceCarRentalForCorporateCustomer(calculatePriceCarRentalForCorporateCustomerRequest).getData();

        price += this.additionalServiceService.calculateAdditionalServicePriceForCorporateCustomer(calculateAdditionalServiceForCorporateCustomerRequest).getData();

        return price;
    }

    private double calculateLateReturnTheCarRentalTotalPriceForCorporateCustomer(CalculatePriceLateReturnTheCarRentalForCorporateCustomerRequest calculatePriceLateReturnTheCarRentalForCorporateCustomerRequest, 
        CalculateAdditionalServiceForCorporateCustomerRequest calculateAdditionalServiceForCorporateCustomerRequest) throws BusinessException
    {
        double price = 0.0;

        price = this.carRentalService.calculatePriceLateReturnTheCarRentalForCorporateCustomer(calculatePriceLateReturnTheCarRentalForCorporateCustomerRequest).getData();

        price += this.additionalServiceService.calculateAdditionalServicePriceForCorporateCustomer(calculateAdditionalServiceForCorporateCustomerRequest).getData();

        return price;
    }

    private double calculateCarRentalTotalPriceForIndividualCustomer(CalculatePriceLateReturnTheCarRentalForIndividualCustomerRequest calculatePriceLateReturnTheCarRentalForIndividualCustomerRequest, CalculateAdditionalServiceForIndividualCustomerRequest calculateAdditionalServiceForIndividualCustomerRequest) throws BusinessException
    {
        double price = 0.0;

        price = this.carRentalService.calculatePriceLateReturnTheCarRentalForIndividualCustomer(calculatePriceLateReturnTheCarRentalForIndividualCustomerRequest).getData();

        price += this.additionalServiceService.calculateAdditionalServicePriceForIndividualCustomer(calculateAdditionalServiceForIndividualCustomerRequest).getData();

        return price;
    }

    private void lateReturnTheRentalCarForCorporateCustomer(LateReturnTheRentalCarForCorporateCustomerRequest lateReturnTheRentalCarForCorporateCustomerRequest)throws BusinessException 
    {
        this.carRentalService.lateReturnTheRentalCarForCorporateCustomer(lateReturnTheRentalCarForCorporateCustomerRequest);
    }
    
    private void lateReturnTheRentalCarForIndividualCustomer(LateReturnTheRentalCarForIndividualCustomerRequest lateReturnTheRentalCarForIndividualCustomerRequest)throws BusinessException 
    {
        this.carRentalService.lateReturnTheRentalCarForIndividualCustomer(lateReturnTheRentalCarForIndividualCustomerRequest);
    }

    private List<Integer> findAdditionalServiceIds(int carRentalId) throws BusinessException
    {
        List<OrderedAdditionalServiceListDto> orderedAdditionalServiceListDtos = this.orderedAdditionalServiceService.getAllByOrderedAdditionalService_CarRentalId(carRentalId).getData();
        
        List<Integer> additionalServiceIds = orderedAdditionalServiceListDtos.stream().map((orderedAdditionalServiceDto) -> orderedAdditionalServiceDto.getAdditionalServiceId()).toList();

        return additionalServiceIds;
    }

    private CarRentalDto returnTheRentalCarForCorporateCustomer(ReturnTheRentalCarForCorporateCustomerRequest returnTheRentalCarForCorporateCustomerRequest) throws BusinessException
    {
       CarRentalDto carRentalDto = this.carRentalService.returnTheRentalCarForCorporateCustomer(returnTheRentalCarForCorporateCustomerRequest).getData();
       return carRentalDto;
    }

    private CarRentalDto returnTheRentalCarForIndividualCustomer(ReturnTheRentalCarForIndividualCustomerRequest returnTheRentalCarForIndividualCustomerRequest) throws BusinessException
    {
       CarRentalDto carRentalDto = this.carRentalService.returnTheRentalCarForIndividualCustomer(returnTheRentalCarForIndividualCustomerRequest).getData();
       return carRentalDto;
    }

    private void updateOfKilometresForCar(UpdateCarKilometerInformationRequest updateCarKilometerInformationRequest) throws BusinessException 
    {
        this.carService.updateCarKilometerInformation(updateCarKilometerInformationRequest);
    }

}