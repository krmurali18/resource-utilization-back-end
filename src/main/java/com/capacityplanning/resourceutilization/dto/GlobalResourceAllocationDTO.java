package com.capacityplanning.resourceutilization.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalResourceAllocationDTO {
    private String resourceName;
    private String groupName;
    private String task;
    private String startDate;
    private String endDate;
    private String projectManager;
    private double fte;
    private String shortDescription;
    private String country;
    private String employementType;
    private String state;
}
