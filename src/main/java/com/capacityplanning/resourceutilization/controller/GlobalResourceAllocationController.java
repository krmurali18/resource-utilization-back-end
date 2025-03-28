package com.capacityplanning.resourceutilization.controller;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import com.capacityplanning.resourceutilization.dto.ResourceAllocationDetailDTO;
import com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDTO;
import com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDetailDTO;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import com.capacityplanning.resourceutilization.service.GlobalResourceAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/global-resource-allocations")
@Tag(name = "global_resource_allocations", description = "Global Resource Allocation API")
public class GlobalResourceAllocationController {


    @Autowired
    GlobalResourceAllocationService globalResourceAllocationService;



    @GetMapping("/")
    @Operation(summary = "Get all global resource allocations", description = "Retrieve a list of all resource allocations")
    public ResponseEntity<List<ProjectResourceMappingDTO>> getGlobalResourceAllocations() {
        return ResponseEntity.ok(globalResourceAllocationService.getGlobalResourceAllocations());
    }


    @PutMapping("/updateResourceAllocation/{id}")
    @Operation(summary = "Update a global resource allocation", description = "Update a specific resource allocation by ID")
    public ResponseEntity<ProjectResourceMappingEntity> updateGlobalResourceAllocation(@PathVariable Long id, @RequestBody ProjectResourceMappingDTO projectResourceMappingDTO) {
        ProjectResourceMappingEntity projectResourceMappingEntity = globalResourceAllocationService.updateGlobalResourceAllocation(id, projectResourceMappingDTO);
        return ResponseEntity.ok(projectResourceMappingEntity);
//        boolean isEdited = globalResourceAllocationService.updateGlobalResourceAllocation(id, projectResourceMappingDTO);
//        if (isEdited) {   
//            return ResponseEntity.ok("Resource allocation edited successfully");
//        } else {
//            return ResponseEntity.status(400).body("Failed to edit resource allocation");
//        }
    }


    @PostMapping("/addResourceAllocation")
    @Operation(summary = "Add a new global resource allocation", description = "Create a new resource allocation entry")
    public ResponseEntity<String> addGlobalResourceAllocation(@RequestBody ProjectResourceMappingDTO projectResourceMappingDTO) {
        System.out.println("projectResourceMappingDTO project Id:"+projectResourceMappingDTO.getProjectInfoEntity().getProjectId());
        System.out.println("projectResourceMappingDTO resourceId:"+projectResourceMappingDTO.getResourceInfoEntity().getResourceId());
        System.out.println("projectResourceMappingDTO resource allocation:"+projectResourceMappingDTO.getStartDate());
        System.out.println("projectResourceMappingDTO resource allocation:"+projectResourceMappingDTO.getAllocationPercentage());
        boolean isAdded = globalResourceAllocationService.addGlobalResourceAllocation(projectResourceMappingDTO);
        if (isAdded) {
            return ResponseEntity.ok("Resource allocation added successfully");
        } else {
            return ResponseEntity.status(400).body("Failed to add resource allocation");
        }
    }

//    @GetMapping("/availableResources")
//    @Operation(summary = "Get available resources", description = "Retrieve a list of resources with allocation percentage more than or equal to 0.5 for a given date range")
//    public ResponseEntity<List<ResourceAvailabilityDTO>> getAvailableResources(
//            @RequestParam String startDate, @RequestParam String endDate) {
//        List<ResourceAvailabilityDTO> availableResources = globalResourceAllocationService.getAvailableResources(startDate, endDate);
//        return ResponseEntity.ok(availableResources);
//    }

    @GetMapping("/availableResources")
    @Operation(summary = "Get available resources", description = "Retrieve a list of resources with allocation percentage less than or equal to 0.5 from today to one year period")
    public ResponseEntity<List<ResourceAvailabilityDTO>> getAvailableResources() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);
        List<ResourceAvailabilityDTO> availableResources = globalResourceAllocationService.getAvailableResources(startDate, endDate);
        return ResponseEntity.ok(availableResources);
    }

    @GetMapping("/monthlyResourceAllocation")
    @Operation(summary = "Get monthly resource allocation", description = "Retrieve the month-wise split of available allocation percentage for each resource for a given time period")
    public ResponseEntity<List<ResourceAllocationDetailDTO>> getMonthlyResourceAllocation(
            @RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<ResourceAllocationDetailDTO> monthlyResourceAllocations = globalResourceAllocationService.getMonthlyResourceAllocation(start, end);
        return ResponseEntity.ok(monthlyResourceAllocations);
    }

}
