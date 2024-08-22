package com.amigoscode.fullstckproject.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        CustomerRowMapper underTest = new CustomerRowMapper();
        ResultSet rs = mock(ResultSet.class);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("name")).thenReturn("mohamed");
        when(rs.getString("email")).thenReturn("mohamed@gmail.com");
        when(rs.getInt("age")).thenReturn(20);
        Customer customer = underTest.mapRow(rs, 0);
        Customer actual = new Customer(1L, "mohamed", "mohamed@gmail.com", 20);
        assertEquals(customer, actual);
    }
}