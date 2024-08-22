package com.amigoscode.fullstckproject.customer;

import com.amigoscode.fullstckproject.exception.DuplicationResourceException;
import com.amigoscode.fullstckproject.exception.RequestValidationException;
import com.amigoscode.fullstckproject.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getCustomers() {
        return customerDao.getCustomers();
    }

    public Customer getCustomerById(Long id) {
        return customerDao.getCustomerById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
    }
    public void insertCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (customerDao.existsByEmail(customerRegistrationRequest.email())) {
            throw new DuplicationResourceException("email taken");
        }

        customerDao.insertCustomer(new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        ));
    }
    @Transactional
    public void deleteCustomerById(Long id) {
        if (!customerDao.existsById(id)){
            throw new ResourceNotFoundException("Customer with id " + id + " not found");
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(CustomerRegistrationRequest customerRegistrationRequest , Long id) {
      Customer customer = getCustomerById(id);
      boolean changes = false;

      if (customerRegistrationRequest.name() !=null && !customerRegistrationRequest.name().equals(customer.getName())) {
          customer.setName(customerRegistrationRequest.name());
          changes = true;
      }
        if (customerRegistrationRequest.age() !=null && !customerRegistrationRequest.age().equals(customer.getAge())) {
            customer.setAge(customerRegistrationRequest.age());
            changes = true;
        }
        if (customerRegistrationRequest.email() !=null && !customerRegistrationRequest.email().equals(customer.getEmail())) {
            if (customerDao.existsByEmail(customerRegistrationRequest.email())) {
                throw new DuplicationResourceException("email taken");
            }
            customer.setEmail(customerRegistrationRequest.email());
            changes = true;
        }
        if (!changes) {
            throw new RequestValidationException("No changes found");
        }
        customerDao.updateCustomer(customer);

    }

}
