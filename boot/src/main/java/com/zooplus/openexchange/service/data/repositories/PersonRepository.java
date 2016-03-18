package com.zooplus.openexchange.service.data.repositories;

import com.zooplus.openexchange.service.data.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
