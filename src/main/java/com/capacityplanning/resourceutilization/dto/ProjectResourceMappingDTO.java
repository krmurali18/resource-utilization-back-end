package com.capacityplanning.resourceutilization.dto;

import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjectResourceMappingDTO {

    @JsonProperty("mapping_id")
    private Long mappingId;

    @JsonProperty("project_id")
    private Integer projectId;

    @JsonProperty("resource_id")
    private Integer resourceId;

    @JsonProperty("allocation_percentage")
    private BigDecimal allocationPercentage;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    // Getters and Setters
    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public BigDecimal getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setAllocationPercentage(BigDecimal allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ProjectResourceMappingDTO(ProjectResourceMappingEntity projectResourceMappingEntity) {
        this.mappingId = projectResourceMappingEntity.getMappingId();
        this.projectId = projectResourceMappingEntity.getProjectId();
        this.resourceId = projectResourceMappingEntity.getResourceId();
        this.allocationPercentage = projectResourceMappingEntity.getAllocationPercentage();
        this.startDate = projectResourceMappingEntity.getStartDate();
        this.endDate = projectResourceMappingEntity.getEndDate();

    }

    public ProjectResourceMappingEntity toEntity() {

        ProjectResourceMappingEntity entity = new ProjectResourceMappingEntity();

        // map fields from DTO to entity
        entity.setMappingId(this.mappingId);
        entity.setProjectId(this.projectId);
        entity.setResourceId(this.resourceId);
        entity.setAllocationPercentage(this.allocationPercentage);
        entity.setStartDate(this.startDate);
        entity.setEndDate(this.endDate);
        
        return entity;

    }
}
