package com.capacityplanning.resourceutilization.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
@Setter
public class ResourceAvailabilityDTO {
    private Integer resourceId;
    private String resourceName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal availability;
    private BigDecimal utilized;

    public ResourceAvailabilityDTO() {
    }

    public ResourceAvailabilityDTO(Integer resourceId,String resourceName, LocalDate startDate, LocalDate endDate, BigDecimal availability,BigDecimal utilized) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.availability = availability;
        this.utilized = utilized;
    }
}
