package com.jesuspacheco.tenpo.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * JPA entity for response_type lookup table.
 */
@Entity
@Table(name = "response_type")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTypeEntity {

    @Id
    @Column(length = 10)
    private String code;

    @Column(length = 50, nullable = false)
    private String description;
}