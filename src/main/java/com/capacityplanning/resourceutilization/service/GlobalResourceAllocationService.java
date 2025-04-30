package com.capacityplanning.resourceutilization.service;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import com.capacityplanning.resourceutilization.dto.ResourceAllocationDetailDTO;
import com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDTO;
import com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDetailDTO;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;

import java.time.LocalDate;
import java.util.List;

public interface GlobalResourceAllocationService {
    // write a method to get all global resource allocations
    public List<ProjectResourceMappingDTO> getGlobalResourceAllocations();

    // write a method to update global resource allocation
    public ProjectResourceMappingEntity updateGlobalResourceAllocation(Long id, ProjectResourceMappingDTO projectResourceMappingDTO);

    // write a method to add global resource allocation
    public ProjectResourceMappingEntity addGlobalResourceAllocation(ProjectResourceMappingDTO projectResourceMappingDTO);

    public List<ResourceAvailabilityDTO> getAvailableResources(LocalDate startDate, LocalDate endDate);

    public List<ResourceAllocationDetailDTO> getMonthlyResourceAllocation(LocalDate startDate, LocalDate endDate);

    public ResourceAllocationDetailDTO getResourceMonthlyAllocation(String resourceName, LocalDate startDate, LocalDate endDate);
}
