package com.turkcell.rentACar.api.controllers.singletonTransactions;

import com.turkcell.rentACar.business.abstracts.singletonTransactions.ColorService;
import com.turkcell.rentACar.business.dtos.color.ColorDto;
import com.turkcell.rentACar.business.dtos.color.ColorListDto;
import com.turkcell.rentACar.business.requests.color.CreateColorRequest;
import com.turkcell.rentACar.business.requests.color.DeleteColorRequest;
import com.turkcell.rentACar.business.requests.color.UpdateColorRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
public class ColorsController 
{
	private ColorService colorService;

	public ColorsController(ColorService colorService) 
	{
		this.colorService = colorService;
	}

	@GetMapping("/getAll")
	DataResult<List<ColorListDto>> getAll()
	{
		return this.colorService.getAll();
	}
	
	@GetMapping("/getById")
	public DataResult<ColorDto> getById(@RequestParam int id) throws BusinessException
	{
		return this.colorService.getById(id);
	}
	
	@PostMapping("/add")
	public Result add(@RequestBody CreateColorRequest createColorRequest) throws BusinessException 
	{
		return this.colorService.add(createColorRequest);
	}
	
	@PutMapping("/update")
	public Result update(@RequestBody UpdateColorRequest updateColorRequest) throws BusinessException
	{
		return this.colorService.update(updateColorRequest);
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody DeleteColorRequest deleteColorRequest) throws BusinessException
	{
		return this.colorService.delete(deleteColorRequest);
	}

}
