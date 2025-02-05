package com.capacityplanning.resourceutilization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "project_resource_mapping")
@Getter
@Setter
public class ProjectResourceMappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mappingId ;

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @Column(name = "resource_id", nullable = false)
    private Integer resourceId;

    @Column(name = "allocation_percentage", nullable = false)
    private BigDecimal allocationPercentage;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
