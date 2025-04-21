package com.capacityplanning.resourceutilization.service.serviceImpl;

import com.capacityplanning.resourceutilization.dto.*;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import com.capacityplanning.resourceutilization.entity.ResourceInfoEntity;
import com.capacityplanning.resourceutilization.entity.ResourceMappingExceptionsEntity;
import com.capacityplanning.resourceutilization.repository.ProjectResourceMappingRepository;
import com.capacityplanning.resourceutilization.repository.ResourceInfoRepository;
import com.capacityplanning.resourceutilization.repository.ResourceMappingExceptionsRepository;
import com.capacityplanning.resourceutilization.service.GlobalResourceAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Autowired
    private ResourceMappingExceptionsRepository resourceMappingExceptionsRepository;

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
            projectResourceMappingEntity.setUpdatedAt(LocalDateTime.now());
            projectResourceMappingEntity.setUpdatedBy("Murali");
            projectResourceMappingEntity.setSource(projectResourceMappingDTO.getSource());
            projectResourceMappingEntity.setResourceId(projectResourceMappingDTO.getResourceId());
            projectResourceMappingEntity.setComments("UPDATED");
        }
        projectResourceMappingEntity =  projectResourceMappingRepository.save(projectResourceMappingEntity);

        if (projectResourceMappingEntity != null) {
            // Assuming you have a ResourceMappingExceptionEntity and its repository
            ResourceMappingExceptionsEntity exceptionEntity = new ResourceMappingExceptionsEntity();
            exceptionEntity.setMappingId(projectResourceMappingEntity.getMappingId());
            exceptionEntity.setResourceId(projectResourceMappingEntity.getResourceId());
            exceptionEntity.setProjectId(projectResourceMappingEntity.getProjectId());
            exceptionEntity.setStartDate(projectResourceMappingEntity.getStartDate());
            exceptionEntity.setEndDate(projectResourceMappingEntity.getEndDate());
            exceptionEntity.setAllocationPercentage(projectResourceMappingEntity.getAllocationPercentage());
            exceptionEntity.setUpdatedAt(LocalDateTime.now());
            exceptionEntity.setUpdatedBy("Murali");
            exceptionEntity.setSource(projectResourceMappingEntity.getSource());
            exceptionEntity.setComments(projectResourceMappingEntity.getComments());
            exceptionEntity.setCreatedAt(LocalDateTime.now());
            exceptionEntity.setCreatedBy("Murali");
            resourceMappingExceptionsRepository.save(exceptionEntity);
        } else {
            throw new RuntimeException("Failed to update the resource allocation. Save operation returned null.");
        }
        return projectResourceMappingEntity;
    }

    @Override
    public ProjectResourceMappingEntity addGlobalResourceAllocation(ProjectResourceMappingDTO projectResourceMappingDTO) {
        ProjectResourceMappingEntity projectResourceMappingEntity = null;
        //boolean recordAdded = false;
        projectResourceMappingEntity = projectResourceMappingRepository.save(projectResourceMappingDTO.toEntity());

        if(projectResourceMappingEntity != null){
            //recordAdded = true;
            ResourceMappingExceptionsEntity exceptionEntity = new ResourceMappingExceptionsEntity();
            exceptionEntity.setMappingId(projectResourceMappingEntity.getMappingId());
            exceptionEntity.setResourceId(projectResourceMappingEntity.getResourceId());
            exceptionEntity.setProjectId(projectResourceMappingEntity.getProjectId());
            exceptionEntity.setStartDate(projectResourceMappingEntity.getStartDate());
            exceptionEntity.setEndDate(projectResourceMappingEntity.getEndDate());
            exceptionEntity.setAllocationPercentage(projectResourceMappingEntity.getAllocationPercentage());
            exceptionEntity.setUpdatedAt(LocalDateTime.now());
            exceptionEntity.setUpdatedBy("Murali");
            exceptionEntity.setSource(projectResourceMappingEntity.getSource());
            exceptionEntity.setComments(projectResourceMappingEntity.getComments());
            exceptionEntity.setCreatedBy("Murali");
            exceptionEntity.setCreatedAt(LocalDateTime.now());
            resourceMappingExceptionsRepository.saveAndFlush(exceptionEntity);
        }
        //System.out.println("recordAdded: " + recordAdded);
        return projectResourceMappingEntity;
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
        resourceInfoDTO.setResourceId(resourceInfoEntity.getResourceId());
        resourceInfoDTO.setResourceName(resourceInfoEntity.getResourceName());
        resourceInfoDTO.setSkills(resourceInfoEntity.getSkills());
        return resourceInfoDTO;
    }

    @Override
    public List<ResourceAllocationDetailDTO> getMonthlyResourceAllocation(LocalDate startDate, LocalDate endDate) {
        List<ResourceAvailabilityDetailDTO> monthlyResourceAllocations = projectResourceMappingRepository.findMonthlyResourceAllocationTotals(startDate, endDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // Initialize a list to hold the final results
        List<ResourceAvailabilityDetailDTO> finalAllocations = new ArrayList<>();
        List<ResourceAllocationDetailDTO> resourceAllocatedDetailList = new ArrayList<>();

        // Iterate through each month in the date range
        List<ResourceInfoEntity> resourceInfoEntities = resourceInfoRepository.findAll();
        startDate.datesUntil(endDate.plusMonths(0), java.time.Period.ofMonths(1)).forEach(date -> {
            String month = date.format(formatter);
            //System.out.println("Month: " + month);
            List<ResourceAvailabilityDetailDTO> allocatedResourceIds = monthlyResourceAllocations.stream()
                    .filter(resource -> (resource.getYearMonth()).equals(month))
                    .collect(Collectors.toList());

            List<ResourceAvailabilityDetailDTO> unallocatedResources = new ArrayList<>();
            resourceInfoEntities.forEach(resource -> {
                if (allocatedResourceIds.stream().noneMatch(r -> r.getResourceId().equals(resource.getResourceId()))) {
                    ResourceAvailabilityDetailDTO noAllocationResource = new ResourceAvailabilityDetailDTO();
                    noAllocationResource.setYearMonth(month);
                    noAllocationResource.setResourceName(resource.getResourceName());
                    noAllocationResource.setResourceId(resource.getResourceId());
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

        startDate.datesUntil(endDate.plusMonths(0), java.time.Period.ofMonths(1)).forEach(date -> {
            String month = date.format(formatter);
            List<AllocationDetailsDTO> allocationDetailDTOLst = new ArrayList<>();

            Map<Integer, List<ResourceAvailabilityDetailDTO>> groupedAllocations = finalAllocations.stream()
                    .collect(Collectors.groupingBy(ResourceAvailabilityDetailDTO::getResourceId));

            groupedAllocations.forEach((resourceId, allocations) -> {
                ResourceAllocationDetailDTO resourceAllocationDetailDTO = new ResourceAllocationDetailDTO();
                resourceAllocationDetailDTO.setResourceId(resourceId);
                resourceAllocationDetailDTO.setResourceName(
                        resourceInfoEntities.stream()
                                .filter(resource -> resource.getResourceId().equals(resourceId))
                                .findFirst()
                                .map(ResourceInfoEntity::getResourceName)
                                .orElse("Unknown")
                );

                List<AllocationDetailsDTO> allocationDetailsDTOList = allocations.stream()
                        .map(allocation -> {
                            AllocationDetailsDTO allocationDetailsDTO = new AllocationDetailsDTO();
                            allocationDetailsDTO.setMonth(java.time.Month.of(Integer.parseInt(allocation.getYearMonth().substring(5))).name().substring(0, 1).toUpperCase() + java.time.Month.of(Integer.parseInt(allocation.getYearMonth().substring(5))).name().substring(1).toLowerCase());
                            allocationDetailsDTO.setYear(allocation.getYearMonth().substring(0, 4));
                            allocationDetailsDTO.setTotalAllocation(allocation.getTotalAllocation());
                            return allocationDetailsDTO;
                        })
                        .collect(Collectors.toList());

                resourceAllocationDetailDTO.setAllocationDetailsDTOList(allocationDetailsDTOList);
                resourceAllocatedDetailList.add(resourceAllocationDetailDTO);
            });

            resourceAllocatedDetailList.forEach(resource -> {
                System.out.println("Resource ID: " + resource.getResourceId());
                System.out.println("Resource Name: " + resource.getResourceName());
                resource.getAllocationDetailsDTOList().forEach(allocation -> {
                    System.out.println("Month: " + allocation.getMonth());
                    System.out.println("Year: " + allocation.getYear());
                    System.out.println("Total Allocation: " + allocation.getTotalAllocation());
                });
            });

        });

        return resourceAllocatedDetailList;
    }
}
