package com.capacityplanning.resourceutilization.dto;

import com.capacityplanning.resourceutilization.entity.ResourceInfoEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceInfoDTO {
    private Integer resourceId;
    private String resourceName;
    private String skills;
    private String company;


    public ResourceInfoDTO() {
    }

    public ResourceInfoDTO(ResourceInfoEntity resourceInfoEntity) {
        this.resourceId = resourceInfoEntity.getResourceId();
        this.resourceName = resourceInfoEntity.getResourceName();
        this.skills = resourceInfoEntity.getSkills();
        this.company = resourceInfoEntity.getCompany();

    }
}
