package com.capacityplanning.resourceutilization.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResourceAllocationDetailDTO {
    private Integer resourceId;
    private String resourceName;
    private List<AllocationDetailsDTO> allocationDetailsDTOList;

    public ResourceAllocationDetailDTO() {
    }

    public ResourceAllocationDetailDTO(Integer resourceId, String resourceName, List<AllocationDetailsDTO> allocationDetailsDTOList) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.allocationDetailsDTOList = allocationDetailsDTOList;
    }
}
