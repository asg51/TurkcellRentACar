package com.turkcell.rentACar.business.constants.messages;

public interface CarRentalMessages 
{
    public static final String CAR_RENTAL_ADDED = "Car Rental Added Succesfully";

    public static final String CAR_RENTAL_ALREADY_EXISTS = "Car Rental Already Exists";
    public static final String CAR_RENTAL_ALREADY_EXISTS_ON_SPECIFIC_DATE = "Car Rental Already Exists On Specific Date";

    public static final String CAR_RENTAL_DELETED = "Car Rental Deleted Succesfully";

    public static final String CAR_RENTAL_GETTED = "Car Rental Getted Succesfully";

    public static final String CAR_RENTAL_LISTED = "Car Rentals Listed Succesfully";

    public static final String CAR_RENTAL_NOT_FOUND = "Car Rental Not Found";

    public static final String START_DATE_MUST_NOT_BE_BEFORE_TODAY = "Start Date Must Be Today Or A Date After Today";

    public static final String START_DATE_MUST_NOT_BE_BEFORE_RETURN_DATE = "Start Date Must Not Be Before Return Date";

    public static final String CAR_RENTAL_UPDATED = "Car Rental Updated Succesfully";
}