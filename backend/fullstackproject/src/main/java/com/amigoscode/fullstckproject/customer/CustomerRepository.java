package com.amigoscode.fullstckproject.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsCustomerByEmail(String email);
    Optional<Customer> findCustomerById(Long id);
    boolean existsCustomerById(Long id);
    void deleteCustomerById(Long id);
}
