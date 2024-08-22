package com.amigoscode.fullstckproject.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> getCustomers() {
        var query = """
         select id,name,email,age from customer
         """;
        return jdbcTemplate.query(query, customerRowMapper);
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        var query = """
         select id,name,email,age from customer where id = ?
         """;

        return jdbcTemplate.query(query, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT into customer(name,email,age)
                VALUES(?,?,?)
                """;
        int result = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
        System.out.println("Rows inserted: " + result);
    }

    @Override
    public boolean existsByEmail(String email) {
        var query = """
                select count(id) from customer where email = ?
                """;
      Integer count =  jdbcTemplate.queryForObject(query, Integer.class, email);
        return count !=null && count > 0;
    }

    @Override
    public boolean existsById(Long id) {
        var query = """
                select count(id) from customer where id = ?
                """;
        Integer count =  jdbcTemplate.queryForObject(query, Integer.class, id);
        return count !=null && count > 0;
    }

    @Override
    public void deleteCustomerById(Long id) {
        var query = """
                delete from customer where id = ?
                """;
       int result = jdbcTemplate.update(query, id);
        System.out.println("the deleted customer is " + result);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getName() != null) {
            var query = """
                    update customer
                    set name = ?
                    where id = ?
                    """;
            int result = jdbcTemplate.update(query, customer.getName(), customer.getId());
            System.out.println("the customer updated name is " + result);
        }

        if (customer.getEmail() != null) {
            var query = """
                    update customer
                    set email = ?
                    where id = ?
                    """;
            int result = jdbcTemplate.update(query, customer.getEmail(), customer.getId());
            System.out.println("the customer updated email is " + result);
        }

        if (customer.getAge() != null) {
            var query = """
                    update customer
                    set age = ?
                    where id = ?
                    """;
            int result = jdbcTemplate.update(query, customer.getAge(), customer.getId());
            System.out.println("the customer updated age is " + result);
        }

    }
}
