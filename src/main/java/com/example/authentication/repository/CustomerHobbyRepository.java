package com.example.authentication.repository;

import com.example.commanentity.Customer_Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerHobbyRepository extends JpaRepository<Customer_Hobby, String> {
}
