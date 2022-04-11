package com.turkcell.rentACar.business.constants.messages;

public interface CarRentalMessages 
{
    public static final String CAR_RENTAL_ADDED = "Car Rental Added Succesfully";

    public static final String CAR_RENTAL_ALREADY_EXISTS = "Car Rental Already Exists";
    public static final String CAR_RENTAL_ALREADY_EXISTS_ON_SPECIFIC_DATE = "Car Rental Already Exists On Specific Date";

    public static final String CAR_RENTAL_NOT_FOUND_ON_SPECIFIC_DATE = "Car Rental Not Found On Specific Date";

    public static final String CAR_RENTAL_DELETED = "Car Rental Deleted Succesfully";

    public static final String CAR_RENTAL_GETTED = "Car Rental Getted Succesfully";

    public static final String CAR_RENTAL_LISTED = "Car Rentals Listed Succesfully";

    public static final String CAR_RENTAL_NOT_FOUND = "Car Rental Not Found";

    public static final String CAR_RENTAL_FOUND = "Car Rental Found";

    public static final String START_DATE_MUST_NOT_BE_BEFORE_TODAY = "Start Date Must Be Today Or A Date After Today";

    public static final String START_DATE_MUST_NOT_BE_BEFORE_RETURN_DATE = "Start Date Must Not Be Before Return Date";

    public static final String CAR_RENTAL_UPDATED = "Car Rental Updated Succesfully";

    public static final String CAR_RENTAL_RETURNED = "Car Rental Returned";

    public static final String CAR_RENTAL_LATE_RETURNED = "Car Rental Late Returned";

    public static final String CAR_RENTAL_CHARGE_CALCULATED = "Car Rental Charge Calculated";

    public static final String CAR_RENTAL_LATE_RETURN_CHARGE_CALCULATED = "Car Rental Late Return Charge Calculated";

    public static final String CAR_RENTAL_CUSTOMER_IS_NOT_THE_SAME = "Car Rental Customer Is Not The Same";

    public static final String CAR_RENTAL_MADE_LATE_RETURN = "Car Rental Made Late Return";

    public static final String CAR_RENTAL_MADE_RETURN = "Car Rental Made Return";
}