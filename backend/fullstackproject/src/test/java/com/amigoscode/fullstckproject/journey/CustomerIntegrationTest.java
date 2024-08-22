package com.amigoscode.fullstckproject.journey;

import com.amigoscode.fullstckproject.customer.Customer;
import com.amigoscode.fullstckproject.customer.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {
    private static final String CUSTOMER_URI = "/v1/api/customers";
    @Autowired
    private WebTestClient webTestClient;
    Random random = new Random();

    @Test
    void canRegisterCustomer() {
        Faker faker = new Faker();
        String name = faker.name().firstName() + faker.name().lastName();
        String email = name + "-" + UUID.randomUUID() + "@amigoscode.com";
        int age = random.nextInt(20, 70);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class).
                exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that customer ris present
        Customer expectedCustomer = new Customer(name, email, age);
        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // get customer by id
        Long id = allCustomers.stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();
        expectedCustomer.setId(id);
        webTestClient.get()
                .uri(CUSTOMER_URI + "/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        Faker faker = new Faker();
        String name = faker.name().firstName() + faker.name().lastName();
        String email = name + "-" + UUID.randomUUID() + "@amigoscode.com";
        int age = random.nextInt(20, 70);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class).
                exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();



        Long id = allCustomers.stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        //delete
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        Faker faker = new Faker();
        String name = faker.name().firstName() + faker.name().lastName();
        String email = name + "-" + UUID.randomUUID() + "@amigoscode.com";
        int age = random.nextInt(20, 70);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class).
                exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();



        Long id = allCustomers.stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        Customer updatedRequest = new Customer("new name", email, age);

        //update
        webTestClient.put()
                .uri(CUSTOMER_URI + "/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expectedCustomer = new Customer(id, "new name", email, age);
        assertThat(expectedCustomer).isEqualTo(updatedCustomer);
    }
}
