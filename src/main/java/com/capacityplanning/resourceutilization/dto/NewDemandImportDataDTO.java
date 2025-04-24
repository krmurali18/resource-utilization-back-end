package com.capacityplanning.resourceutilization.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NewDemandImportDataDTO {
    private String shortDescription;
    private String task;
    private String resourcePlan;
    private String demandManager;
    private String projectManager;
    private String groupName;
    private String state;
    private String portfolio;
    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal fte;

    public NewDemandImportDataDTO() {
    }

    public NewDemandImportDataDTO(String shortDescription, String task, String resourcePlan,
                                  String demandManager, String projectManager, String groupName, String state,
                                  String portfolio, LocalDate startDate, LocalDate endDate, BigDecimal fte) {
        this.shortDescription = shortDescription;
        this.task = task;
        this.resourcePlan = resourcePlan;
        this.demandManager = demandManager;
        this.projectManager = projectManager;
        this.groupName = groupName;
        this.state = state;
        this.portfolio = portfolio;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fte = fte;
    }
}
