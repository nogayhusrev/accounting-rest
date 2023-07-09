package com.nogayhusrev.accountingrest.repository;

import com.nogayhusrev.accountingrest.entity.Category;
import com.nogayhusrev.accountingrest.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByCompany(Company company);
}
