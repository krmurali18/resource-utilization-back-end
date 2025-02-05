package com.capacityplanning.resourceutilization.entity;

import javax.persistence.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Entity
@Table(name = "project_resource_mapping")
public class ProjectResourceMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "resource_id", nullable = false)
    private String resourceId;

    @Column(name = "quantity_allocated", nullable = false)
    private BigDecimal quantityAllocated;

    @Column(name = "allocation_date", nullable = false)
    private LocalDate allocationDate;
}
