package com.nogayhusrev.accountingrest.repository;

import com.nogayhusrev.accountingrest.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//    List<Product> findAllByCategory_Company();

}
