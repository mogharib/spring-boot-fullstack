package com.amigoscode.fullstckproject.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository("list")

public class CustomerListDataAccessService implements CustomerDao {

    @Override
    public List<Customer> getCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customers.stream().filter(customer -> customer.getId().equals(id)).findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customers.stream().anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public boolean existsById(Long id) {
        return customers.stream().anyMatch(customer -> customer.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Long id) {
        customers.stream().filter(customer -> customer.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }

    private static List<Customer> customers;
    static  {
        customers = new ArrayList<>();
        Customer john =  new Customer(11L, "John", "j@j.com", 20);
        Customer jane =  new Customer(2L, "Jane", "j@j.com", 21);
        customers.add(jane);
        customers.add(john);
    }
}
