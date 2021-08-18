package com.example.authentication.repository;

import com.example.commanentity.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HobbyRepository extends JpaRepository<Hobby, String> {
    Hobby findByName (String name);
}
