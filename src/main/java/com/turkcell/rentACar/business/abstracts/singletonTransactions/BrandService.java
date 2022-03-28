package com.turkcell.rentACar.business.abstracts.singletonTransactions;


import com.turkcell.rentACar.business.dtos.brand.BrandDto;
import com.turkcell.rentACar.business.dtos.brand.BrandListDto;
import com.turkcell.rentACar.business.requests.brand.CreateBrandRequest;
import com.turkcell.rentACar.business.requests.brand.DeleteBrandRequest;
import com.turkcell.rentACar.business.requests.brand.UpdateBrandRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface BrandService {
    DataResult<List<BrandListDto>> getAll();
    Result add(CreateBrandRequest createBrandRequest) throws BusinessException;
    DataResult<BrandDto> getById(int id) throws BusinessException;
    Result update(UpdateBrandRequest updateBrandRequest) throws BusinessException;
    Result delete(DeleteBrandRequest deleteBrandRequest) throws BusinessException;
}
