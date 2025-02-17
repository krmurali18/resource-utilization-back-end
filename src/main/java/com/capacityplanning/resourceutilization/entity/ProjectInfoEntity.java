package com.capacityplanning.resourceutilization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_info")
@Getter
@Setter
public class ProjectInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Column(name="skill")
    private String skill;

    @Column(name="start_date", nullable = false)
    private LocalDate startDate;

    @Column(name="end_date", nullable = false)
    private LocalDate endDate;

    @Column(name="description")
    private String description;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="created_by", nullable = false)
    private String createdBy;

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name="updated_by", nullable = false)
    private String updatedBy;

}
