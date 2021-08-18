package com.example.authentication.repository;

import com.example.commanentity.Vendor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends CrudRepository<Vendor, String> {
    Vendor findByEmailid(String email);

    int countByEmailid(String email);
}
