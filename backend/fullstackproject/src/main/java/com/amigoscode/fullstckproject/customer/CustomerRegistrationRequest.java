package com.amigoscode.fullstckproject.customer;

public record CustomerRegistrationRequest (
        String name,
        String email,
        Integer age
){
}
