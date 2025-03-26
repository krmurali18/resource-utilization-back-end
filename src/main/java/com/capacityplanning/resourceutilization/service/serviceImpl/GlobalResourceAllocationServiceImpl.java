package com.capacityplanning.resourceutilization.service.serviceImpl;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDTO;
import com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDetailDTO;
import com.capacityplanning.resourceutilization.dto.ResourceInfoDTO;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import com.capacityplanning.resourceutilization.entity.ResourceInfoEntity;
import com.capacityplanning.resourceutilization.repository.ProjectResourceMappingRepository;
import com.capacityplanning.resourceutilization.repository.ResourceInfoRepository;
import com.capacityplanning.resourceutilization.service.GlobalResourceAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
public class GlobalResourceAllocationServiceImpl implements GlobalResourceAllocationService {

    @Autowired
    private ProjectResourceMappingRepository projectResourceMappingRepository;

    @Autowired
    private ResourceInfoRepository resourceInfoRepository;

    @Override
    public List<ProjectResourceMappingDTO> getGlobalResourceAllocations() {
        return projectResourceMappingRepository.findAll().stream().map(ProjectResourceMappingDTO::new).collect(Collectors.toList());
    }

    @Override
    public ProjectResourceMappingEntity updateGlobalResourceAllocation(Long id, ProjectResourceMappingDTO projectResourceMappingDTO) {

        Optional<ProjectResourceMappingEntity> projectResourceMappingEntityOptional = projectResourceMappingRepository.findById(id);
        ProjectResourceMappingEntity projectResourceMappingEntity = null;
        if (projectResourceMappingEntityOptional.isPresent()) {
            projectResourceMappingEntity = projectResourceMappingEntityOptional.get();
            projectResourceMappingEntity.setAllocationPercentage(projectResourceMappingDTO.getAllocationPercentage());
            projectResourceMappingEntity.setUpdatedBy("Murali");
            projectResourceMappingEntity.setResourceId(projectResourceMappingDTO.getResourceId());
        }
        return projectResourceMappingRepository.save(projectResourceMappingEntity);
//        } else {
//            // Handle the case where the entity is not found
//            throw new Exception("Entity with id " + id + " not found");
//        }
    }

    @Override
    public boolean addGlobalResourceAllocation(ProjectResourceMappingDTO projectResourceMappingDTO) {
        return projectResourceMappingRepository.saveAndFlush(projectResourceMappingDTO.toEntity()) != null;
    }

    @Override
    public List<ResourceAvailabilityDTO> getAvailableResources(LocalDate startDate, LocalDate endDate) {
        return projectResourceMappingRepository.findAvailableResourcesForDateRange(startDate, endDate);

        // return projectResourceMappingRepository.findAvailableResourcesForDateRange(start, end)
        //     .stream()
        //     .map(resourceId -> {
        //     ResourceInfoEntity resourceInfoEntity = resourceInfoRepository.findById(resourceId).orElseThrow(() -> new RuntimeException("Resource not found"));
        //     return convertToResourceInfoDTO(resourceInfoEntity);
        //     })
        //     .collect(Collectors.toList());
    }

    private ResourceInfoDTO convertToResourceInfoDTO(ResourceInfoEntity resourceInfoEntity){

        ResourceInfoDTO resourceInfoDTO = new ResourceInfoDTO();
        resourceInfoDTO.setResourceId(resourceInfoEntity.getId());
        resourceInfoDTO.setResourceName(resourceInfoEntity.getResourceName());
        resourceInfoDTO.setSkills(resourceInfoEntity.getSkills());
        return resourceInfoDTO;
    }

    @Override
    public List<ResourceAvailabilityDetailDTO> getMonthlyResourceAllocation(LocalDate startDate, LocalDate endDate) {
        List<ResourceAvailabilityDetailDTO> monthlyResourceAllocations = projectResourceMappingRepository.findMonthlyResourceAllocationTotals(startDate, endDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // Initialize a list to hold the final results
        List<ResourceAvailabilityDetailDTO> finalAllocations = new ArrayList<>();

        // Iterate through each month in the date range
        List<ResourceInfoEntity> resourceInfoEntities = resourceInfoRepository.findAll();
        startDate.datesUntil(endDate.plusMonths(1), java.time.Period.ofMonths(1)).forEach(date -> {
            String month = date.format(formatter);
            //System.out.println("Month: " + month);
            List<ResourceAvailabilityDetailDTO> allocatedResourceIds = monthlyResourceAllocations.stream()
                    .filter(resource -> (resource.getYearMonth()).equals(month))
                    .collect(Collectors.toList());

            List<ResourceAvailabilityDetailDTO> unallocatedResources = new ArrayList<>();
            resourceInfoEntities.forEach(resource -> {
                if (allocatedResourceIds.stream().noneMatch(r -> r.getResourceId().equals(resource.getId()))) {
                    ResourceAvailabilityDetailDTO noAllocationResource = new ResourceAvailabilityDetailDTO();
                    noAllocationResource.setYearMonth(month);
                    noAllocationResource.setResourceName(resource.getResourceName());
                    noAllocationResource.setResourceId(resource.getId());
                    noAllocationResource.setTotalAllocation(0.0);
                    unallocatedResources.add(noAllocationResource);
                    //allocatedResourceIds.add(noAllocationResource);
                    //System.out.println("Resource ID: " + resource.getId() + " has no allocation for month: " + month);
                }
            });

            finalAllocations.addAll(allocatedResourceIds);
            finalAllocations.addAll(unallocatedResources);
            finalAllocations.sort((r1, r2) -> {
                int resourceIdComparison = r1.getResourceId().compareTo(r2.getResourceId());
                if (resourceIdComparison != 0) {
                    return resourceIdComparison;
                }
                return r1.getYearMonth().compareTo(r2.getYearMonth());
            });
        });
        return finalAllocations;
    }
}
