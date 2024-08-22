package com.amigoscode.fullstckproject.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;


class CustomerJpaDataAccessServiceTest {
    private CustomerJpaDataAccessService underTest;
    @Mock
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getCustomers() {
        //when
        underTest.getCustomers();

        //then
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById() {
        Long id = 1L;

        //when
        underTest.getCustomerById(id);
        //then
        verify(customerRepository).findCustomerById(id);
    }

    @Test
    void insertCustomer() {
        Customer customer = new Customer(2L, "mohamed", "mohamed@gmail.com", 20);
        //when
        underTest.insertCustomer(customer);
        //verify
        verify(customerRepository).save(customer);
    }

    @Test
    void existsByEmail() {
        String email = "mohamed@gmail.com";
        underTest.existsByEmail(email);
        //verify
        verify(customerRepository).existsCustomerByEmail(email);

    }

    @Test
    void existsById() {
        Long id = 2L;
        underTest.existsById(id);
        //verify
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        Long id = 2L;
        underTest.deleteCustomerById(id);
        //verify
        verify(customerRepository).deleteCustomerById(id);
    }

    @Test
    void updateCustomer() {
        Customer customer = new Customer(2L, "mohamed", "mohamed@gmail.com", 20);
        //when
        underTest.insertCustomer(customer);
        //verify
        verify(customerRepository).save(customer);
    }
}