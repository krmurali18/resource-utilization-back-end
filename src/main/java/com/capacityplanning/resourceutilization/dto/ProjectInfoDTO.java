package com.capacityplanning.resourceutilization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;

@Getter
@Setter
public class ProjectInfoDTO {
    private Integer projectId;
    private String groupName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    private String task;
    private String status;
    private BigDecimal requiredAllocation;
    private String projectManager;
    private String demandManager;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

    public ProjectInfoDTO() {

    }

    // Method to convert Entity to DTO
    public ProjectInfoDTO(ProjectInfoEntity entity) {
        this.projectId = entity.getProjectId();
        this.groupName = entity.getGroupName();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.description = entity.getDescription();
        this.task = entity.getTask();
        this.status = entity.getStatus();
        this.requiredAllocation = entity.getRequiredAllocation();
        this.demandManager = entity.getDemandManager();
        this.projectManager = entity.getProjectManager();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.createdBy = entity.getCreatedBy();
        this.updatedBy = entity.getUpdatedBy();

    }
}
