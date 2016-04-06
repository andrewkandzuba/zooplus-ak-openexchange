package com.zooplus.openexchange.service.frontend.database.repositories;

import com.zooplus.openexchange.service.frontend.database.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
