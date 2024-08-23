package com.amigoscode.fullstckproject;

import com.amigoscode.fullstckproject.customer.Customer;
import com.amigoscode.fullstckproject.customer.CustomerRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@SpringBootApplication
@RestController
public class FullStackProjectApplication {


	public static void main(String[] args) {
		SpringApplication.run(FullStackProjectApplication.class, args);
	}

	@Bean
	CommandLineRunner t(CustomerRepository customerRepository) {
		return args -> {
		var faker = new Faker();
		Name name = faker.name();
		String firstName = name.firstName();
		String lastName = name.lastName();
		Random random = new Random();
		Customer customer = new Customer(
				firstName + " " + lastName,
				firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com",
				random.nextInt(18,70)
		);
		customerRepository.save(customer);
		};
	}
}
