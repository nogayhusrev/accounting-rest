package com.nogayhusrev.accounting_rest.repository;

import com.nogayhusrev.accounting_rest.entity.Category;
import com.nogayhusrev.accounting_rest.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByCompany(Company company);
}
