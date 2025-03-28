package com.capacityplanning.resourceutilization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resource_info")
@Getter
@Setter
public class ResourceInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Integer resourceId;

    @Column(name = "resource_name", nullable = false)
    private String resourceName;

    @Column(name = "skills", nullable = false)
    private String skills;

    @Column(name="company")
    private String company;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_at", nullable = false)
    private String updatedAt;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

}
