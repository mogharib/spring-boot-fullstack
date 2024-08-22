package com.amigoscode.fullstckproject.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> getCustomers();

    Optional<Customer> getCustomerById(Long id);
    void insertCustomer(Customer customer);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    void deleteCustomerById(Long id);
    void updateCustomer(Customer customer);
}
