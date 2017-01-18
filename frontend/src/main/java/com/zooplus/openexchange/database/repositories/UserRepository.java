package com.zooplus.openexchange.database.repositories;

import com.zooplus.openexchange.database.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    User findByNameAndPassword(String name, String password);
}
