package com.capacityplanning.resourceutilization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "project_resource_mapping")
@Getter
@Setter
public class ProjectResourceMappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    private Long mappingId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "allocation_percentage", nullable = false)
    private BigDecimal allocationPercentage;

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id", insertable = false, updatable = false)
    private ProjectInfoEntity projectInfoEntity;

    @Column(name = "resource_id", nullable = false)
    private Integer resourceId;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "resource_id", insertable = false, updatable = false)
    private ResourceInfoEntity resourceInfoEntity;

    @OneToMany(mappedBy = "projectResourceAllocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectResourceAllocationEntity> projectResourceAllocationList;
}
