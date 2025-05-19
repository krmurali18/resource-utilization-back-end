package com.capacityplanning.resourceutilization.dto;

import com.capacityplanning.resourceutilization.entity.ResourceInfoEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResourceInfoDTO {
    private Integer resourceId;
    private String resourceName;
    private String company;

    private String country;

    private String employeeType;

    private boolean active;

    private boolean superUser;

    private String createdBy;

    private String updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public ResourceInfoDTO() {
    }

    public ResourceInfoDTO(ResourceInfoEntity resourceInfoEntity) {
        this.resourceId = resourceInfoEntity.getResourceId();
        this.resourceName = resourceInfoEntity.getResourceName();
        this.company = resourceInfoEntity.getCompany();
        this.country = resourceInfoEntity.getCountry();
        this.employeeType = resourceInfoEntity.getEmployeeType();
        this.active = resourceInfoEntity.getActive();
        this.superUser = resourceInfoEntity.getSuperUser();
        this.createdBy = resourceInfoEntity.getCreatedBy();
        this.updatedBy = resourceInfoEntity.getUpdatedBy();
        this.createdAt = resourceInfoEntity.getCreatedAt();
        this.updatedAt = resourceInfoEntity.getUpdatedAt();

    }
}
