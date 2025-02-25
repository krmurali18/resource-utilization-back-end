package com.capacityplanning.resourceutilization.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.NotFound;
import java.math.BigDecimal;

@Entity
@Table(name = "project_resource_allocation")
@Getter
@Setter
public class ProjectResourceAllocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapping_id", referencedColumnName = "mapping_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProjectResourceMappingEntity projectResourceAllocation;

    @Column(name = "allocation_percentage", nullable = false)
    private BigDecimal allocationPercentage;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "year", nullable = false)
    private int year;

}
