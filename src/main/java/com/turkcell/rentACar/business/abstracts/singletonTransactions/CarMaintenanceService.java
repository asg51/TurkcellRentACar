package com.turkcell.rentACar.business.abstracts.singletonTransactions;



import com.turkcell.rentACar.business.dtos.carMaintenance.CarMaintenanceListDto;
import com.turkcell.rentACar.business.requests.carMaintenance.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.requests.carMaintenance.DeleteCarMaintenanceRequest;
import com.turkcell.rentACar.business.requests.carMaintenance.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.utilities.exceptions.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.entities.concretes.CarMaintenance;

import java.util.Date;
import java.util.List;

public interface CarMaintenanceService 
{
    DataResult<List<CarMaintenanceListDto>> getAll();
    Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest)  throws BusinessException;
    Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException;
    Result delete(DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) throws BusinessException;
    DataResult<List<CarMaintenanceListDto>> getByCarId(int id) throws BusinessException;
    DataResult<CarMaintenance> getByCarIdAndReturnDate(int carId,Date returnDate);

    Result checkIfItIsMaintainableByMaintenance(int carId) throws BusinessException;
}
