package com.capacityplanning.resourceutilization.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "project_resource_mapping")
public class ProjectResourceMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mappingId ;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "resource_id", nullable = false)
    private String resourceId;

    @Column(name = "allocation_percentage", nullable = false)
    private BigDecimal allocationPercentage;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
