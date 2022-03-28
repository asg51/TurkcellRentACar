package com.turkcell.rentACar.api.controllers.singletonTransactions;

import com.turkcell.rentACar.business.abstracts.singletonTransactions.BrandService;
import com.turkcell.rentACar.business.dtos.brand.BrandDto;
import com.turkcell.rentACar.business.dtos.brand.BrandListDto;
import com.turkcell.rentACar.business.requests.brand.CreateBrandRequest;
import com.turkcell.rentACar.business.requests.brand.DeleteBrandRequest;
import com.turkcell.rentACar.business.requests.brand.UpdateBrandRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandsController 
{
	
    private BrandService brandService;

    public BrandsController(BrandService brandService) 
    {
        this.brandService = brandService;
    }

    @GetMapping("/getAll")
    public DataResult<List<BrandListDto>> getAll()
    {
        return this.brandService.getAll();
    }

    @GetMapping("/getById")
    public DataResult<BrandDto> getById(@RequestParam int id) throws BusinessException
    {
    	return this.brandService.getById(id);
    }

    @PostMapping("/add")
    public Result add(@RequestBody CreateBrandRequest createBrandRequest) throws BusinessException 
    {
        return this.brandService.add(createBrandRequest);
    }
    
    @PutMapping("/update")
    public Result update(@RequestBody UpdateBrandRequest updateBrandRequest) throws BusinessException
    {
        return this.brandService.update(updateBrandRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody DeleteBrandRequest deleteBrandRequest) throws BusinessException
    {
        return this.brandService.delete(deleteBrandRequest);
    }
}
