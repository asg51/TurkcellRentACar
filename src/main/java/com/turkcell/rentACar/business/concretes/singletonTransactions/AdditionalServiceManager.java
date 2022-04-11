package com.turkcell.rentACar.business.concretes.singletonTransactions;

import java.util.List;
import java.util.stream.Collectors;

import com.turkcell.rentACar.business.abstracts.singletonTransactions.AdditionalServiceService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.additionalService.AdditionalServiceDto;
import com.turkcell.rentACar.business.dtos.additionalService.AdditionalServiceListDto;
import com.turkcell.rentACar.business.requests.additionalService.CalculateAdditionalServiceForCorporateCustomerRequest;
import com.turkcell.rentACar.business.requests.additionalService.CalculateAdditionalServiceForIndividualCustomerRequest;
import com.turkcell.rentACar.business.requests.additionalService.CreateAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.additionalService.DeleteAdditionalServiceRequest;
import com.turkcell.rentACar.business.requests.additionalService.UpdateAdditionalServiceRequest;
import com.turkcell.rentACar.business.utilities.dateOperations;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.AdditionalServiceDao;
import com.turkcell.rentACar.entities.concretes.AdditionalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdditionalServiceManager implements AdditionalServiceService 
{
    private AdditionalServiceDao additionalServiceDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public AdditionalServiceManager(AdditionalServiceDao additionalServiceDao, ModelMapperService modelMapperService) 
    {
        this.additionalServiceDao = additionalServiceDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest) throws BusinessException 
    {    
        checkIfExistAdditionalServiceByName(createAdditionalServiceRequest.getAdditionalServiceName());

        AdditionalService additionalService = this.modelMapperService.forRequest().map(createAdditionalServiceRequest,AdditionalService.class);

        this.additionalServiceDao.save(additionalService);

        return new SuccessResult(BusinessMessages.ADDITIONAL_SERVICE_ADDED);
    }

    @Override
    public Result update(UpdateAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException 
    {    
        checkIfExistByAdditionalServiceById(updateAdditionalServiceRequest.getAdditionalServiceId());
        checkIfExistAdditionalServiceByName(updateAdditionalServiceRequest.getAdditionalServiceName());

        AdditionalService additionalService = this.additionalServiceDao.getById(updateAdditionalServiceRequest.getAdditionalServiceId());
        additionalService = this.modelMapperService.forRequest().map(updateAdditionalServiceRequest,AdditionalService.class);

        this.additionalServiceDao.save(additionalService);

        return new SuccessResult(BusinessMessages.ADDITIONAL_SERVICE_UPDATED);
    }

    @Override
    public Result delete(DeleteAdditionalServiceRequest deleteAdditionalServiceRequest) throws BusinessException 
    {   
        checkIfExistByAdditionalServiceById(deleteAdditionalServiceRequest.getOrderedAdditionalServiceId());

        AdditionalService additionalService = this.modelMapperService.forRequest().map(deleteAdditionalServiceRequest,AdditionalService.class);

        this.additionalServiceDao.delete(additionalService);

        return new SuccessResult(BusinessMessages.ADDITIONAL_SERVICE_DELETED);
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getAll() 
    {    
        List<AdditionalService> result = this.additionalServiceDao.findAll();
        List<AdditionalServiceListDto> response = result.stream().map(additionalService -> this.modelMapperService.forDto().map(additionalService, AdditionalServiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<AdditionalServiceListDto>>(response, BusinessMessages.ADDITIONAL_SERVICE_LISTED);
    }

    @Override
    public DataResult<AdditionalServiceDto> getById(int additionalServiceId) throws BusinessException 
    {    
        checkIfExistByAdditionalServiceById(additionalServiceId);

        AdditionalService additionalService = additionalServiceDao.getById(additionalServiceId);

        AdditionalServiceDto additionalServiceDto = this.modelMapperService.forDto().map(additionalService,AdditionalServiceDto.class);

        return new SuccessDataResult<AdditionalServiceDto>(additionalServiceDto, BusinessMessages.ADDITIONAL_SERVICE_GETTED);
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getAdditionalServicesByIds(List<Integer> additionalServicesIds) throws BusinessException 
    {
        checkIfExistByAdditionalServiceByIds(additionalServicesIds);
        
        var result = this.additionalServiceDao.getAdditionalServicesByIds(additionalServicesIds);

        List<AdditionalServiceListDto> response = result.stream().map(additionalService -> this.modelMapperService.forDto().map(additionalService, AdditionalServiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<AdditionalServiceListDto>>(response, BusinessMessages.ADDITIONAL_SERVICE_LISTED);
    }

    @Override
    public DataResult<Double> calculateAdditionalServicePriceForCorporateCustomer(CalculateAdditionalServiceForCorporateCustomerRequest calculateAdditionalServiceForCorporateCustomerRequest) throws BusinessException
    {
        checkIfExistByAdditionalServiceByIds(calculateAdditionalServiceForCorporateCustomerRequest.getAdditionalServiceIds());

        double price = 0.0;

        long days= dateOperations.findTheNumberOfDaysToRent(calculateAdditionalServiceForCorporateCustomerRequest.getStartDate(),calculateAdditionalServiceForCorporateCustomerRequest.getReturnDate());

        var additionalServiceListDtos = getAdditionalServicesByIds(calculateAdditionalServiceForCorporateCustomerRequest.getAdditionalServiceIds()).getData();
        
        for (var additionalService : additionalServiceListDtos) 
        {
            price += additionalService.getDailyPrice() * days;
        }

        return new SuccessDataResult<>(price, BusinessMessages.ADDITIONAL_SERVICE_CHARGE_CALCULATED);
    }

    @Override
    public DataResult<Double> calculateAdditionalServicePriceForIndividualCustomer(CalculateAdditionalServiceForIndividualCustomerRequest calculateAdditionalServiceForIndividualCustomerRequest) throws BusinessException
    {
        checkIfExistByAdditionalServiceByIds(calculateAdditionalServiceForIndividualCustomerRequest.getAdditionalServiceIds());

        double price = 0.0;

        long days= dateOperations.findTheNumberOfDaysToRent(calculateAdditionalServiceForIndividualCustomerRequest.getStartDate(),
        calculateAdditionalServiceForIndividualCustomerRequest.getReturnDate());

        var additionalServiceListDtos = getAdditionalServicesByIds(calculateAdditionalServiceForIndividualCustomerRequest.getAdditionalServiceIds()).getData();
        
        for (var additionalService : additionalServiceListDtos) 
        {
            price += additionalService.getDailyPrice() * days;
        }

        return new SuccessDataResult<>(price, BusinessMessages.ADDITIONAL_SERVICE_CHARGE_CALCULATED);
    }

    @Override
    public Result checkIfExistByAdditionalServiceById(int additionalServiceId) throws BusinessException 
    {    
        if (!this.additionalServiceDao.existsById(additionalServiceId)) 
        {
            throw new BusinessException(BusinessMessages.ADDITIONAL_SERVICE_NOT_FOUND);
        }

        return new SuccessResult(BusinessMessages.ADDITIONAL_SERVICE_FOUND);
    }

    private void checkIfExistByAdditionalServiceByIds(List<Integer> additionalServiceId) throws BusinessException 
    {    
        var additionalServiceListDtos = getAdditionalServicesByIds(additionalServiceId).getData();

        if (additionalServiceListDtos.size()!=additionalServiceId.size()) 
        {
            throw new BusinessException(BusinessMessages.ADDITIONAL_SERVICE_NOT_FOUND);
        }
    }
    
    private void checkIfExistAdditionalServiceByName(String additionalServiceName) throws BusinessException 
    {    
        if (this.additionalServiceDao.existsByAdditionalServiceName(additionalServiceName)) 
        {
            throw new BusinessException(BusinessMessages.ADDITIONAL_SERVICE_ALREADY_EXISTS);
        }
    }
}