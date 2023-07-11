package com.nogayhusrev.accounting_rest.repository;

import com.nogayhusrev.accounting_rest.entity.Company;
import com.nogayhusrev.accounting_rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    User findUserByUsername(String username);

    List<User> findAllByRole_Description(String roleDescription);

    List<User> findAllByCompany(Company company);
}
