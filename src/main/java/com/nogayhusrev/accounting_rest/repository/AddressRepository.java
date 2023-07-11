package com.nogayhusrev.accounting_rest.repository;

import com.nogayhusrev.accounting_rest.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
