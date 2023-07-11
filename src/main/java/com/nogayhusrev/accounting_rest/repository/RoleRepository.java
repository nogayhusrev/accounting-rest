package com.nogayhusrev.accounting_rest.repository;

import com.nogayhusrev.accounting_rest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(Long id);

    Role findByDescription(String description);
}
