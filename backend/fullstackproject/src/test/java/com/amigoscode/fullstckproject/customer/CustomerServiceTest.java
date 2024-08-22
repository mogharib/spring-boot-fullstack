package com.amigoscode.fullstckproject.customer;

import com.amigoscode.fullstckproject.exception.DuplicationResourceException;
import com.amigoscode.fullstckproject.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;


    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getCustomers() {
    }

    @Test
    void getCustomerByIdExists() {
        Long id = 1L;
        Customer customer = new Customer(id, "mohamed", "mohamed@gmail.com", 20);
        //when
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        //then
        Customer actual = underTest.getCustomerById(id);
        assert actual.equals(customer);
    }

    @Test
    void getCustomerByIdIsNotExists() {
        Long id = 1L;
        //when
        when(customerDao.getCustomerById(id)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> underTest.getCustomerById(id)).
                isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id " + id + " not found");
    }

    @Test
    void insertCustomer() {
        String email = "mohamed@gmail.com";
        when(customerDao.existsByEmail(email)).thenReturn(false);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest("mohamed", email, 20);
        //when
        underTest.insertCustomer(customerRegistrationRequest);
        //then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer actual = customerArgumentCaptor.getValue();
         assertThat(actual.getId()).isEqualTo(null);
        assertThat(actual.getName()).isEqualTo(customerRegistrationRequest.name());
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getAge()).isEqualTo(customerRegistrationRequest.age());

    }

    @Test
    void insertCustomerThrowException() {
        String email = "mohamed@gmail.com";
        when(customerDao.existsByEmail(email)).thenReturn(true);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest("mohamed", email, 20);
        //then
        assertThatThrownBy(() -> underTest.insertCustomer(customerRegistrationRequest))
                .isInstanceOf(DuplicationResourceException.class)
                .hasMessage("email taken");
        verify(customerDao,never()).insertCustomer(any());

    }

    @Test
    void deleteCustomerById() {
        Long id = 10L;

        //when
        when(customerDao.existsById(id)).thenReturn(true);

        underTest.deleteCustomerById(id) ;
        //then
        verify(customerDao).deleteCustomerById(id);

    }

    @Test
    void deleteCustomerByIdThrowException() {
        Long id = 10L;

        //when
        when(customerDao.existsById(id)).thenReturn(false);
        //then
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id " + id + " not found");
        //then
        verify(customerDao,never()).deleteCustomerById(id);

    }



    @Test
    void updateCustomer() {
        Long id = 10L;
        String email = "mohamed@gmail.com";
        String newEmail = "mohamed2@gmail.com";
        Customer customer = new Customer(id, "mohamed", email, 20);
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest("mohamed mido", newEmail , 30);
        when(customerDao.existsByEmail(newEmail)).thenReturn(false);
        //when
        underTest.updateCustomer(customerRegistrationRequest,id);
        //then
        verify(customerDao).updateCustomer(customer);
    }
}