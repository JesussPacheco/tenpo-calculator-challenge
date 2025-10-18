package com.jesuspacheco.tenpo.infrastructure.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * JPA entity for calculation_history table.
 */
@Entity
@Table(name = "calculation_history")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String endpoint;

    @Column(length = 8, nullable = false)
    private String method;

    @Column(nullable = false)
    private Integer status;

    @Column(name = "response_type_code", length = 10, nullable = false)
    private String responseTypeCode;

    @Type(JsonBinaryType.class)
    @Column(name = "request_params", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> requestParams;

    @Type(JsonBinaryType.class)
    @Column(name = "outcome_json", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> outcomeJson;

    @Column(name = "executed_at", nullable = false)
    private LocalDateTime executedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
