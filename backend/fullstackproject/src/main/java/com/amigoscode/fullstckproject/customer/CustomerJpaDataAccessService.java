package com.amigoscode.fullstckproject.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository("jpa")
public class CustomerJpaDataAccessService implements CustomerDao {
    private final CustomerRepository customerRepository;

    public CustomerJpaDataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findCustomerById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existsById(Long id) {
        return customerRepository.existsCustomerById(id);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteCustomerById(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
         customerRepository.save(customer);
    }
}
