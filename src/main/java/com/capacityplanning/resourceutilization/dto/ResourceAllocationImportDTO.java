package com.capacityplanning.resourceutilization.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class ResourceAllocationImportDTO {

    private String resourceName;
    private String task;
    private String groupName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String projectManager;
    private double fte;
    private String shortDescription;
    private String country;
    private String employmentType;
    private String state;
}
