package com.capacityplanning.resourceutilization.service;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import java.util.List;

public interface GlobalResourceAllocationService {
    // write a method to get all global resource allocations
    public List<ProjectResourceMappingDTO> getGlobalResourceAllocations();
}
