package com.capacityplanning.resourceutilization.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllocationDetailsDTO {
    private String month;
    private String year;
    private Double totalAllocation;

    public AllocationDetailsDTO() {
    }

    public AllocationDetailsDTO(String month, String year, Double totalAllocation) {
        this.month = month;
        this.year = year;
        this.totalAllocation = totalAllocation;
    }


}
