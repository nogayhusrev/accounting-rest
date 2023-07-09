package com.nogayhusrev.accountingrest.repository;

import com.nogayhusrev.accountingrest.entity.ClientVendor;
import com.nogayhusrev.accountingrest.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {
    List<ClientVendor> findAllByCompany(Company company);
}
