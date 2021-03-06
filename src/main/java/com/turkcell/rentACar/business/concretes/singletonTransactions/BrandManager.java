package com.turkcell.rentACar.business.concretes.singletonTransactions;

import com.turkcell.rentACar.business.abstracts.singletonTransactions.BrandService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.brand.BrandDto;
import com.turkcell.rentACar.business.dtos.brand.BrandListDto;
import com.turkcell.rentACar.business.requests.brand.CreateBrandRequest;
import com.turkcell.rentACar.business.requests.brand.DeleteBrandRequest;
import com.turkcell.rentACar.business.requests.brand.UpdateBrandRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.BrandDao;
import com.turkcell.rentACar.entities.concretes.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandManager implements BrandService 
{
	private BrandDao brandDao;
	private ModelMapperService modelMapperService;

	@Autowired
	public BrandManager(BrandDao brandDao,ModelMapperService modelMapperService) 
	{
		this.brandDao = brandDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<BrandListDto>> getAll() 
	{
		List<Brand> result = this.brandDao.findAll();
		List<BrandListDto> response = result.stream().map(brand->this.modelMapperService.forDto().map(brand,BrandListDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<BrandListDto>>(response,BusinessMessages.BRAND_LISTED);
	}

	@Override
	public Result add(CreateBrandRequest createBrandRequest) throws BusinessException 
	{
		checkIfExistBrandName(createBrandRequest.getBrandName());

		Brand brand = this.modelMapperService.forRequest().map(createBrandRequest,Brand.class);
		this.brandDao.save(brand);

		return new SuccessResult(BusinessMessages.BRAND_ADDED);
	}

	@Override
	public DataResult<BrandDto> getById(int id) throws BusinessException 
	{
		checkIfExistBrandId(id);

		Brand brand = brandDao.getById(id);
		BrandDto brandDto = this.modelMapperService.forDto().map(brand,BrandDto.class);

		return new SuccessDataResult<BrandDto>(brandDto,BusinessMessages.BRAND_GETTED);
	}

	@Override
	public Result update(UpdateBrandRequest updateBrandRequest) throws BusinessException 
	{
		checkIfExistBrandId(updateBrandRequest.getBrandId());
		checkIfExistBrandName(updateBrandRequest.getBrandName());

		Brand brand = this.brandDao.getById(updateBrandRequest.getBrandId());
		brand = this.modelMapperService.forRequest().map(updateBrandRequest,Brand.class);
		this.brandDao.save(brand);

		return new SuccessResult(BusinessMessages.BRAND_UPDATED);
	}

	@Override
	public Result delete(DeleteBrandRequest deleteBrandRequest) throws BusinessException
	{
		checkIfExistBrandId(deleteBrandRequest.getBrandId());

		Brand brand = this.modelMapperService.forRequest().map(deleteBrandRequest,Brand.class);
		this.brandDao.delete(brand);

		return new SuccessResult(BusinessMessages.BRAND_DELETED);
	}

    @Override
	public Result checkIfExistBrandId(int brandId) throws BusinessException 
	{
		if(!this.brandDao.existsById(brandId)) 
		{
			throw new BusinessException(BusinessMessages.BRAND_NOT_FOUND);
		}

        return new SuccessResult(BusinessMessages.BRAND_FOUND);
	}

	private void checkIfExistBrandName(String brandName) throws BusinessException 
	{
		if(this.brandDao.existsByBrandName(brandName)) 
		{
			throw new BusinessException(BusinessMessages.BRAND_ALREADY_EXISTS);
		}	
	}
}
