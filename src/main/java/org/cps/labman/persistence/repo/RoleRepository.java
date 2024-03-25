package org.cps.labman.persistence.repo;

import org.cps.labman.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>
{

    Role findByName(String name);
}
