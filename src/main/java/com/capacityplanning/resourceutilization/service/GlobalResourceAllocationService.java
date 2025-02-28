package com.capacityplanning.resourceutilization.service;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import java.util.List;

public interface GlobalResourceAllocationService {
    // write a method to get all global resource allocations
    public List<ProjectResourceMappingDTO> getGlobalResourceAllocations();

    // write a method to update global resource allocation
    public ProjectResourceMappingDTO updateGlobalResourceAllocation(Long id, ProjectResourceMappingDTO projectResourceMappingDTO);

    // write a method to add global resource allocation
    public boolean addGlobalResourceAllocation(ProjectResourceMappingDTO projectResourceMappingDTO);
}
