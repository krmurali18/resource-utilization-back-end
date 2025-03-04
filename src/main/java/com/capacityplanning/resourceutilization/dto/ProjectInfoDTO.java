package com.capacityplanning.resourceutilization.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;

@Getter
@Setter
public class ProjectInfoDTO {
    private Integer projectId;
    private String groupName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String skill;

    private String task;

    public ProjectInfoDTO() {

    }


    // Method to convert Entity to DTO
    public ProjectInfoDTO(ProjectInfoEntity entity) {
        this.projectId = entity.getProjectId();
        this.groupName = entity.getGroupName();
        this.skill = entity.getSkill();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.description = entity.getDescription();
        this.task = entity.getTask();
    }
}
