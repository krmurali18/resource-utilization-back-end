package com.capacityplanning.resourceutilization.service;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDTO;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;

import java.util.List;

public interface GlobalResourceAllocationService {
    // write a method to get all global resource allocations
    public List<ProjectResourceMappingDTO> getGlobalResourceAllocations();

    // write a method to update global resource allocation
    public ProjectResourceMappingEntity updateGlobalResourceAllocation(Long id, ProjectResourceMappingDTO projectResourceMappingDTO);

    // write a method to add global resource allocation
    public boolean addGlobalResourceAllocation(ProjectResourceMappingDTO projectResourceMappingDTO);

    public List<ResourceAvailabilityDTO> getAvailableResources(String startDate, String endDate);

    // write a method to get new projects
    public List<ProjectResourceMappingDTO> getNewProjects();
}
