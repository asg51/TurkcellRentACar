package com.turkcell.rentACar.api.controllers.singletonTransactions;

import com.turkcell.rentACar.business.abstracts.singletonTransactions.CarMaintenanceService;
import com.turkcell.rentACar.business.dtos.carMaintenance.CarMaintenanceListDto;
import com.turkcell.rentACar.business.requests.carMaintenance.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.requests.carMaintenance.DeleteCarMaintenanceRequest;
import com.turkcell.rentACar.business.requests.carMaintenance.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/carMaintenances")
public class CarMaintenancesController 
{
    private CarMaintenanceService carMaintenanceService;

    public CarMaintenancesController(CarMaintenanceService carMaintenanceService) 
    {
        this.carMaintenanceService = carMaintenanceService;
    }
    
    @GetMapping("/getAll")
    DataResult<List<CarMaintenanceListDto>> getAll()
    {
    	return carMaintenanceService.getAll();
    }
    
    @PostMapping("/add")
    Result add(@RequestBody CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException
    {
    	return carMaintenanceService.add(createCarMaintenanceRequest);
    }
    
    @PutMapping("/update")
    Result update(@RequestBody UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException
    {
    	return carMaintenanceService.update(updateCarMaintenanceRequest);
    }
    
    @DeleteMapping("/delete")
    Result delete(@RequestBody DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) throws BusinessException
    {
    	return carMaintenanceService.delete(deleteCarMaintenanceRequest);
    }
    @GetMapping("/getByCarId")
    DataResult<List<CarMaintenanceListDto>> getByCarId(@RequestParam int id) throws BusinessException
    {
    	return carMaintenanceService.getByCarId(id);
    }
}
