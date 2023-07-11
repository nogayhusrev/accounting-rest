package com.nogayhusrev.accounting_rest.repository;

import com.nogayhusrev.accounting_rest.entity.ClientVendor;
import com.nogayhusrev.accounting_rest.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {
    List<ClientVendor> findAllByCompany(Company company);
}
