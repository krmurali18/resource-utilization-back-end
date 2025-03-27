package com.capacityplanning.resourceutilization.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "resource_mapping_exceptions")
@Getter
@Setter
public class ResourceMappingExceptionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    private Long mappingId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "allocation_percentage", nullable = false)
    private BigDecimal allocationPercentage;

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id", insertable = false, updatable = false)
    private ProjectInfoEntity projectInfoEntity;

    @Column(name = "resource_id", nullable = false)
    private Integer resourceId;

    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "resource_id", insertable = false, updatable = false)
    private ResourceInfoEntity resourceInfoEntity;

}
