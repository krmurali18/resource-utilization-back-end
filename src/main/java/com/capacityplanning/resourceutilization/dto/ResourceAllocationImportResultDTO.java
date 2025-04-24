package com.capacityplanning.resourceutilization.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ResourceAllocationImportResultDTO {
    private Integer rowNum;
    private String resourceName;
    private String groupName;
    private String task;
    private LocalDate startDate;
    private LocalDate endDate;
    private String projectManager;
    private double fte;
    private String shortDescription;
    private String country;
    private String employmentType;
    private String state;
    private String importStatus;
    private String message;

    public ResourceAllocationImportResultDTO() {
    }

    public ResourceAllocationImportResultDTO(String importStatus, String message) {
        this.importStatus = importStatus;
        this.message = message;
    }

}
