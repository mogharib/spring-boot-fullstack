package com.amigoscode.fullstckproject.customer;

import com.amigoscode.fullstckproject.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {
    @Autowired
    private CustomerRepository underTest;
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsByEmail() {
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                faker.number().numberBetween(16, 80)
        );
        underTest.save(customer);
        boolean actual = underTest.existsCustomerByEmail(customer.getEmail());
        assertTrue(actual);
    }

    @Test
    void existsByEmailReturnFalse() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        boolean actual = underTest.existsCustomerByEmail(email);
        assertFalse(actual);
    }

    @Test
    void findCustomerById() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                26
        );
        underTest.save(customer);
        Long id = underTest.findAll()
                .stream().
                filter(c -> c.getEmail().equals(email)).
                map(Customer::getId)
                .findFirst().orElseThrow();

        Optional<Customer> actual = underTest.findCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getName()).isEqualTo(customer.getName());
        } );
    }

    @Test
    void existsById() {
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                faker.number().numberBetween(16, 80)
        );
        underTest.save(customer);
        Long id = underTest.findAll()
                .stream().
                filter(c -> c.getEmail().equals(customer.getEmail())).
                map(Customer::getId)
                .findFirst().orElseThrow();
        boolean actual = underTest.existsCustomerById(id);
        assertTrue(actual);

    }

    @Test
    void existsByIdReturnFalse() {
        Long id =  -1L;
        boolean actual = underTest.existsCustomerById(id);
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
        underTest.save(customer);
        Long id = underTest.findAll()
                .stream().
                filter(c -> c.getEmail().equals(customer.getEmail())).
                map(Customer::getId)
                .findFirst().orElseThrow();
        // when
        underTest.deleteCustomerById(id);
        //then
        Optional<Customer> actual = underTest.findCustomerById(id);
        assertThat(actual.isPresent()).isFalse();
    }
}