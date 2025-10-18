package com.jesuspacheco.tenpo.infrastructure.adapter.out.persistence;

import com.jesuspacheco.tenpo.domain.model.CalculationHistory;
import com.jesuspacheco.tenpo.domain.port.out.HistoryRepository;
import com.jesuspacheco.tenpo.infrastructure.adapter.out.persistence.entity.HistoryEntity;
import com.jesuspacheco.tenpo.infrastructure.adapter.out.persistence.mapper.HistoryEntityMapper;
import com.jesuspacheco.tenpo.infrastructure.adapter.out.persistence.repository.HistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adapter that implements HistoryRepository port using JPA.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HistoryPersistenceAdapter implements HistoryRepository {

    private final HistoryJpaRepository jpaRepository;
    private final HistoryEntityMapper entityMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CalculationHistory save(CalculationHistory history) {
        log.debug("Saving history for endpoint={}", history.getEndpoint());

        HistoryEntity entity = entityMapper.toEntity(history);
        HistoryEntity saved = jpaRepository.save(entity);

        log.debug("Saved with id={}", saved.getId());
        return entityMapper.toDomain(saved);
    }

    @Override
    public Page<CalculationHistory> findAll(Pageable pageable) {
        log.debug("Querying history page={} size={}", pageable.getPageNumber(), pageable.getPageSize());

        return jpaRepository.findAll(pageable)
                .map(entityMapper::toDomain);
    }
}