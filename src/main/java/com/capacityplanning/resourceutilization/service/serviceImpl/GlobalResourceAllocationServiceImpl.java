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
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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
//        return projectResourceMappingRepository.findByStatus("Allocated").stream()
//                .map(ProjectResourceMappingDTO::new)
//                .collect(Collectors.toList());
        return projectResourceMappingRepository.findAllocatedResourcesWithActiveFlag().stream()
                .map(ProjectResourceMappingDTO::new)
                .collect(Collectors.toList());
        //return projectResourceMappingRepository.findAll().stream().map(ProjectResourceMappingDTO::new).collect(Collectors.toList());
    }

    @Override
    public ProjectResourceMappingEntity updateGlobalResourceAllocation(Long id, ProjectResourceMappingDTO projectResourceMappingDTO) {

        ProjectResourceMappingEntity projectResourceMappingEntity = null;
        if(id!=null) {
            Optional<ProjectResourceMappingEntity> projectResourceMappingEntityOptional = projectResourceMappingRepository.findById(id);
            if (projectResourceMappingEntityOptional.isPresent()){
                projectResourceMappingEntity = projectResourceMappingEntityOptional.get();
                projectResourceMappingEntity.setAllocationPercentage(projectResourceMappingDTO.getAllocationPercentage());
                projectResourceMappingEntity.setUpdatedAt(LocalDateTime.now());
                projectResourceMappingEntity.setUpdatedBy("Murali");
                projectResourceMappingEntity.setSource(projectResourceMappingDTO.getSource());
                projectResourceMappingEntity.setResourceId(projectResourceMappingDTO.getResourceId());
                projectResourceMappingEntity.setComments(projectResourceMappingDTO.getComments());
            } else if (projectResourceMappingEntityOptional.isEmpty()) {
                projectResourceMappingEntity = projectResourceMappingRepository.findByResourceIdAndProjectIdAndStartDateAndEndDate(
                        projectResourceMappingDTO.getResourceId(),
                        projectResourceMappingDTO.getProjectId(),
                        projectResourceMappingDTO.getStartDate(),
                        projectResourceMappingDTO.getEndDate()
                );
                if(projectResourceMappingEntity == null) {
                    projectResourceMappingEntity = new ProjectResourceMappingEntity();
                    projectResourceMappingEntity.setStartDate(projectResourceMappingDTO.getStartDate());
                    projectResourceMappingEntity.setEndDate(projectResourceMappingDTO.getEndDate());
                    projectResourceMappingEntity.setAllocationPercentage(projectResourceMappingDTO.getAllocationPercentage());
                    projectResourceMappingEntity.setProjectId(projectResourceMappingDTO.getProjectId());
                    projectResourceMappingEntity.setResourceId(projectResourceMappingDTO.getResourceId());
                    projectResourceMappingEntity.setSource(projectResourceMappingDTO.getSource());
                    projectResourceMappingEntity.setStatus("Allocated");
                    projectResourceMappingEntity.setCreatedAt(LocalDateTime.now());
                    projectResourceMappingEntity.setCreatedBy("Murali");
                } else {
                    projectResourceMappingEntity.setAllocationPercentage(projectResourceMappingDTO.getAllocationPercentage());
                    projectResourceMappingEntity.setUpdatedAt(LocalDateTime.now());
                    projectResourceMappingEntity.setUpdatedBy("Murali");
                    projectResourceMappingEntity.setSource(projectResourceMappingDTO.getSource());
                    projectResourceMappingEntity.setResourceId(projectResourceMappingDTO.getResourceId());
                    projectResourceMappingEntity.setComments(projectResourceMappingDTO.getComments());
                }
            }
        }

        projectResourceMappingEntity = projectResourceMappingRepository.save(projectResourceMappingEntity);

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
            List<ResourceAvailabilityDetailDTO> allocatedResourceIds = monthlyResourceAllocations.stream()
                    .filter(resource -> (resource.getYearMonth()).equals(month))
                    .collect(Collectors.toList());

            List<ResourceAvailabilityDetailDTO> unallocatedResources = new ArrayList<>();
            resourceInfoEntities.forEach(resource -> {
                if (allocatedResourceIds.stream().noneMatch(r -> r.getResourceId().equals(resource.getResourceId())) || monthlyResourceAllocations.stream().noneMatch(r -> r.getResourceId().equals(resource.getResourceId()))) {
                    ResourceAvailabilityDetailDTO noAllocationResource = new ResourceAvailabilityDetailDTO();
                    noAllocationResource.setYearMonth(month);
                    noAllocationResource.setResourceName(resource.getResourceName());
                    noAllocationResource.setResourceId(resource.getResourceId());
                    noAllocationResource.setTotalAllocation(0.0);
                    unallocatedResources.add(noAllocationResource);
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

//            resourceAllocatedDetailList.forEach(resource -> {
//                System.out.println("Resource ID: " + resource.getResourceId());
//                System.out.println("Resource Name: " + resource.getResourceName());
//                resource.getAllocationDetailsDTOList().forEach(allocation -> {
//                    System.out.println("Month: " + allocation.getMonth());
//                    System.out.println("Year: " + allocation.getYear());
//                    System.out.println("Total Allocation: " + allocation.getTotalAllocation());
//                });
//            });

        });

        return resourceAllocatedDetailList;
    }

    @Override
    public ResourceAllocationDetailDTO getResourceMonthlyAllocation(String resourceName, LocalDate startDate, LocalDate endDate) {
        List<ResourceAvailabilityDetailDTO> monthlyResourceAllocations = projectResourceMappingRepository.findMonthlyResourceAllocationTotalsByResourceName(startDate, endDate,resourceName);
        Map<String, List<ResourceAvailabilityDetailDTO>> groupedAllocations = monthlyResourceAllocations.stream()
                .collect(Collectors.groupingBy(ResourceAvailabilityDetailDTO::getYearMonth));


        ResourceAllocationDetailDTO resourceAllocationDetailDTO = new ResourceAllocationDetailDTO();
        if(!monthlyResourceAllocations.isEmpty())
            resourceAllocationDetailDTO.setResourceId(monthlyResourceAllocations.get(0).getResourceId());
        resourceAllocationDetailDTO.setResourceName(resourceName);
        List<AllocationDetailsDTO> allocationDetailsDTOs = monthlyResourceAllocations.stream().map(allocation -> {
            AllocationDetailsDTO allocationDetailsDTO = new AllocationDetailsDTO();
            allocationDetailsDTO.setMonth(Month.of(Integer.parseInt(allocation.getYearMonth().substring(5))).name().substring(0, 1).toUpperCase() + Month.of(Integer.parseInt(allocation.getYearMonth().substring(5))).name().substring(1).toLowerCase());
            allocationDetailsDTO.setYear(allocation.getYearMonth().substring(0, 4));
            allocationDetailsDTO.setTotalAllocation(allocation.getTotalAllocation());
            return allocationDetailsDTO;
        }).collect(Collectors.toList());
        resourceAllocationDetailDTO.setAllocationDetailsDTOList(allocationDetailsDTOs);

        startDate.datesUntil(endDate.plusMonths(1), java.time.Period.ofMonths(1)).forEach(date -> {
            String month = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            boolean allocationExists = false;
            allocationExists = resourceAllocationDetailDTO.getAllocationDetailsDTOList().stream()
                    .anyMatch(allocation -> allocation.getYear().equals(month.substring(0, 4)) &&
                            allocation.getMonth().equals(java.time.Month.of(Integer.parseInt(month.substring(5))).name().substring(0, 1).toUpperCase() +
                                    java.time.Month.of(Integer.parseInt(month.substring(5))).name().substring(1).toLowerCase()));

            if (!allocationExists) {

                AllocationDetailsDTO allocationDetailsDTO = new AllocationDetailsDTO();
                allocationDetailsDTO.setMonth(java.time.Month.of(Integer.parseInt(month.substring(5))).name().substring(0, 1).toUpperCase() +
                        java.time.Month.of(Integer.parseInt(month.substring(5))).name().substring(1).toLowerCase());
                allocationDetailsDTO.setYear(month.substring(0, 4));
                allocationDetailsDTO.setTotalAllocation(0.0);
                resourceAllocationDetailDTO.getAllocationDetailsDTOList().add(allocationDetailsDTO);
            }
        });

        return resourceAllocationDetailDTO;
    }

}

