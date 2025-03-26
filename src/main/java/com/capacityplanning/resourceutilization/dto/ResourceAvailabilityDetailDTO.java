package com.capacityplanning.resourceutilization.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ResourceAvailabilityDetailDTO {
    private Integer resourceId;

    private String yearMonth;
    private Double totalAllocation;

    public ResourceAvailabilityDetailDTO() {
    }

    public ResourceAvailabilityDetailDTO(Integer resourceId, String yearMonth, BigDecimal totalAllocation) {
        this.resourceId = resourceId;
        this.yearMonth = yearMonth;
        this.totalAllocation = totalAllocation.doubleValue();
    }

}
