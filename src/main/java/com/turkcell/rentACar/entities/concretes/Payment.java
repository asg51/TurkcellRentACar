package com.turkcell.rentACar.entities.concretes;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class Payment 
{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
    private int paymentId;
        
    @OneToOne()
    @JoinColumn(name = "customer_id" )
    private Customer customer;

	@OneToOne(mappedBy = "payment")
    private CarRental carRental;
}
