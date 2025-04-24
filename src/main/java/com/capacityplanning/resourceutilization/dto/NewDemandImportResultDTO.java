package com.capacityplanning.resourceutilization.dto;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NewDemandImportResultDTO {
    private Integer rowNum;
    private BigDecimal fte;
    private String requestNumber;
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
    private String importStatus;
    private String message;

    public NewDemandImportResultDTO() {
    }

    public NewDemandImportResultDTO(String importStatus, String message) {
        this.importStatus = importStatus;
        this.message = message;
    }
}
