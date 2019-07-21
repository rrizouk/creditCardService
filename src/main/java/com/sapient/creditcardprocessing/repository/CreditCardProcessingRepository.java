package com.sapient.creditcardprocessing.repository;

import com.sapient.creditcardprocessing.domain.CreditCardDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;

@PersistenceContext(unitName = "creditCardDetails")
@Repository
public interface CreditCardProcessingRepository extends JpaRepository<CreditCardDetails,String> {

    CreditCardDetails findByCustomerName(@Param("customerName") String customerName);
}
