package com.capacityplanning.resourceutilization.dto;

import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;
import com.capacityplanning.resourceutilization.entity.ProjectResourceAllocationEntity;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import com.capacityplanning.resourceutilization.entity.ResourceInfoEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectResourceMappingDTO {

    @JsonProperty("mapping_id")
    private Long mappingId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("project_id")
    private Integer projectId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("projectInfoEntity")
    private ProjectInfoEntity projectInfoEntity;

    @JsonProperty("resourceInfoEntity")
    private ResourceInfoEntity resourceInfoEntity;

    @JsonProperty("resource_id")
    private Integer resourceId;

    @JsonProperty("resource_name")
    private String resourceName;


    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("allocation_percentage")
    private BigDecimal allocationPercentage;

    @JsonProperty("task")
    private String task;

    @JsonProperty("source")
    private String source;

    /*@JsonProperty("projectResourceAllocation")
    private List<ProjectResourceAllocationEntity> projectResourceAllocationEntity;*/


    public ProjectResourceMappingDTO(){

    }

    public ProjectResourceMappingDTO(ProjectResourceMappingEntity projectResourceMappingEntity) {
        this.mappingId = projectResourceMappingEntity.getMappingId();
        this.projectId = projectResourceMappingEntity.getProjectInfoEntity().getProjectId();
        this.groupName = projectResourceMappingEntity.getProjectInfoEntity().getGroupName();
        this.resourceId = projectResourceMappingEntity.getResourceInfoEntity().getId();
        this.resourceName = projectResourceMappingEntity.getResourceInfoEntity().getResourceName();
        this.description = projectResourceMappingEntity.getProjectInfoEntity().getDescription();
        this.startDate = projectResourceMappingEntity.getStartDate();
        this.endDate = projectResourceMappingEntity.getEndDate();
        this.task = projectResourceMappingEntity.getProjectInfoEntity().getTask();
        this.allocationPercentage = projectResourceMappingEntity.getAllocationPercentage();
        this.source = projectResourceMappingEntity.getSource();
        //this.projectResourceAllocationEntity = projectResourceMappingEntity.getProjectResourceAllocationList();

    }

    public ProjectResourceMappingEntity toEntity() {

        ProjectResourceMappingEntity entity = new ProjectResourceMappingEntity();

        // map fields from DTO to entity
        entity.setProjectId(this.getProjectInfoEntity().getProjectId());
        entity.setResourceId(this.getResourceInfoEntity().getId());
        //entity.setProjectInfoEntity(this.getProjectInfoEntity());
        //entity.setResourceInfoEntity(this.getResourceInfoEntity());
        entity.setStartDate(this.startDate);
        entity.setEndDate(this.endDate);
        entity.setAllocationPercentage(this.allocationPercentage);
        entity.setSource(this.source);
        entity.setCreatedBy("Murali");
        entity.setUpdatedBy("Murali");

        return entity;

    }
}
