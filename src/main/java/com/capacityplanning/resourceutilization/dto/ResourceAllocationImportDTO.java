package com.capacityplanning.resourceutilization.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ResourceAllocationImportDTO {

    private String startDate;
    private String endDate;
    private String projectManager;
    private double fte;
    private String shortDescription;
    private String country;
    private String employementType;
    private String state;
}
