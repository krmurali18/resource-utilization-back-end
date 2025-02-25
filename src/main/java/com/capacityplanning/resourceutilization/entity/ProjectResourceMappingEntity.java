package com.capacityplanning.resourceutilization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @Column(name = "resource_id", nullable = false)
    private Integer resourceId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id", insertable = false, updatable = false)
    private ProjectInfoEntity projectInfoEntity;

    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "resource_id", insertable = false, updatable = false)
    private ResourceInfoEntity resourceInfoEntity;

    @OneToMany(mappedBy = "projectResourceAllocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectResourceAllocationEntity> projectResourceAllocationList;
}
