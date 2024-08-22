package com.amigoscode.fullstckproject.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return  customerService.getCustomers();
    }

    @GetMapping("/customers/{id}")
    public Customer getCustomers(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }
    @PostMapping("/customers")
    public void createCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
         customerService.insertCustomer(customerRegistrationRequest);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) {
         customerService.deleteCustomerById(id);
    }

    @PutMapping("/customers/{id}")
    public void createCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest,@PathVariable Long id) {
        customerService.updateCustomer(customerRegistrationRequest,id);
    }
}
