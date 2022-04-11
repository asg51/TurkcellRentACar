package com.turkcell.rentACar.business.abstracts.singletonTransactions;

import com.turkcell.rentACar.business.dtos.color.ColorDto;
import com.turkcell.rentACar.business.dtos.color.ColorListDto;
import com.turkcell.rentACar.business.requests.color.CreateColorRequest;
import com.turkcell.rentACar.business.requests.color.DeleteColorRequest;
import com.turkcell.rentACar.business.requests.color.UpdateColorRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface ColorService 
{
	DataResult<List<ColorListDto>> getAll();
	Result add(CreateColorRequest createColorRequest) throws BusinessException;
	DataResult<ColorDto> getById(int id) throws BusinessException;
	Result update(UpdateColorRequest updateColorRequest) throws BusinessException;
	Result delete(DeleteColorRequest DeleteColorRequest) throws BusinessException;

    Result checkIfExistByColorId(int colorId) throws BusinessException;
}
