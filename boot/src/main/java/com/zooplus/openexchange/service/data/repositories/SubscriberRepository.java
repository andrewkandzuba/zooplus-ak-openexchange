package com.zooplus.openexchange.service.data.repositories;

import com.zooplus.openexchange.service.data.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    Subscriber findByEmail(String email);
}
