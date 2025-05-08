package com.capacityplanning.resourceutilization.dto;
import com.capacityplanning.resourceutilization.entity.ResourceMappingExceptionsEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ResourceMappingExceptionDTO {
    private Integer id;
    private Long mappingId;
    private Integer projectId;
    private String groupName;

    private String description;

    private String resourceName;

    private String task;
    private Integer resourceId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String source;
    private String comments;
    private String createdBy;
    private String updatedBy;
    private String createdAt;
    private String updatedAt;

    private BigDecimal allocationPercentage;

    public ResourceMappingExceptionDTO() {
    }

    public ResourceMappingExceptionDTO(ResourceMappingExceptionsEntity entity) {
        this.id = entity.getId();
        this.mappingId = entity.getMappingId();
        this.projectId = entity.getProjectId();
        this.resourceId = entity.getResourceId();

        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.source = entity.getSource();
        this.comments = entity.getComments();
        this.createdBy = entity.getCreatedBy();
        this.updatedBy = entity.getUpdatedBy();
        this.createdAt = entity.getCreatedAt().toString();
        this.updatedAt = entity.getUpdatedAt().toString();
        this.groupName = entity.getProjectInfoEntity().getGroupName();
        this.description = entity.getProjectInfoEntity().getDescription();
        this.task = entity.getProjectInfoEntity().getTask();
        this.resourceName = entity.getResourceInfoEntity().getResourceName();
        this.allocationPercentage = entity.getAllocationPercentage();
    }

}
