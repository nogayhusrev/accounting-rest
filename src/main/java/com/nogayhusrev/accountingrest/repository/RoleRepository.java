package com.nogayhusrev.accountingrest.repository;

import com.nogayhusrev.accountingrest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(Long id);

    Role findByDescription(String description);
}
