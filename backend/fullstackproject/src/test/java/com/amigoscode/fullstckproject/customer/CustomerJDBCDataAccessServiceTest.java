package com.amigoscode.fullstckproject.customer;

import com.amigoscode.fullstckproject.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {
    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper);
    }

    @Test
    void getCustomers() {
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                faker.number().numberBetween(16, 80)
        );
        underTest.insertCustomer(customer);
        List<Customer> customers = underTest.getCustomers();
        assertThat(customers).isNotEmpty();
    }

    @Test
    void getCustomerById() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                26
        );
        underTest.insertCustomer(customer);
        Long id = underTest.getCustomers()
                .stream().
                filter(c -> c.getEmail().equals(email)).
                map(Customer::getId)
                .findFirst().orElseThrow();

        Optional<Customer> actual = underTest.getCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getName()).isEqualTo(customer.getName());
        } );
    }

    @Test
    void willReturnEmptyWithNonExistentId() {
        Long id = -1L;
        Optional<Customer> actual = underTest.getCustomerById(id);
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                faker.number().numberBetween(16, 80)
        );
        underTest.insertCustomer(customer);
        Long id = underTest.getCustomers()
                .stream().
                filter(c -> c.getEmail().equals(customer.getEmail())).
                map(Customer::getId)
                .findFirst().orElseThrow();
        // when
        if (!underTest.existsByEmail(customer.getEmail())) {
            underTest.insertCustomer(customer);
        }

        boolean actual = underTest.existsById(id);
        assertTrue(actual);
    }

    @Test
    void existsByEmail() {
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                faker.number().numberBetween(16, 80)
        );
        underTest.insertCustomer(customer);
        boolean actual = underTest.existsByEmail(customer.getEmail());
        assertTrue(actual);
    }

    @Test
    void existsByEmailReturnFalse() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        boolean actual = underTest.existsByEmail(email);
        assertFalse(actual);
    }

    @Test
    void existsById() {
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                faker.number().numberBetween(16, 80)
        );
        underTest.insertCustomer(customer);
        Long id = underTest.getCustomers()
                .stream().
                filter(c -> c.getEmail().equals(customer.getEmail())).
                map(Customer::getId)
                .findFirst().orElseThrow();
        boolean actual = underTest.existsById(id);
        assertTrue(actual);

    }

    @Test
    void existsByIdReturnFalse() {
        Long id =  -1L;
        boolean actual = underTest.existsById(id);
        assertFalse(actual);
    }

    @Test
    void deleteCustomerById() {
        //given
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                faker.number().numberBetween(16, 80)
        );
        underTest.insertCustomer(customer);
        Long id = underTest.getCustomers()
                .stream().
                filter(c -> c.getEmail().equals(customer.getEmail())).
                map(Customer::getId)
                .findFirst().orElseThrow();
        // when
        underTest.deleteCustomerById(id);
        //then
        Optional<Customer> actual = underTest.getCustomerById(id);
        assertThat(actual.isPresent()).isFalse();
    }

    @Test
    void updateCustomer() {
        //given
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                faker.number().numberBetween(16, 80)
        );
        underTest.insertCustomer(customer);
        Long id = underTest.getCustomers()
                .stream().
                filter(c -> c.getEmail().equals(customer.getEmail())).
                map(Customer::getId)
                .findFirst().orElseThrow();
        //when
        String name = "updatedName";
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setName(name);
        underTest.updateCustomer(updatedCustomer);
        //then
        Optional<Customer> actual = underTest.getCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getName()).isEqualTo(name);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getId()).isEqualTo(id);
        });
    }
}