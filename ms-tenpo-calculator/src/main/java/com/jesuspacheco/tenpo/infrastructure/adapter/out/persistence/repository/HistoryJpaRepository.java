package com.jesuspacheco.tenpo.infrastructure.adapter.out.persistence.repository;

import com.jesuspacheco.tenpo.infrastructure.adapter.out.persistence.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for history entities.
 */
@Repository
public interface HistoryJpaRepository extends JpaRepository<HistoryEntity, Long> {
}