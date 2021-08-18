package com.example.authentication.repository;

import com.example.commanentity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByEmailid(String email);

    int countByEmailid(String email);
}
