package com.nogayhusrev.accountingrest.repository;

import com.nogayhusrev.accountingrest.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
