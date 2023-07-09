package com.nogayhusrev.accountingrest.repository;

import com.nogayhusrev.accountingrest.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByTitle(String title);

}
