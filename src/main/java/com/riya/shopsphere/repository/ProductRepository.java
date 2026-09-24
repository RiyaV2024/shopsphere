package com.riya.shopsphere.repository;

import com.riya.shopsphere.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
