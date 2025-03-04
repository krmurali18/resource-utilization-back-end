package com.capacityplanning.resourceutilization.controller;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import com.capacityplanning.resourceutilization.service.GlobalResourceAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // public List<ProjectResourceMappingDTO> getGlobalResourceAllocations() {
    //   return globalResourceAllocationService.getGlobalResourceAllocations();
    //}

    @PostMapping("/addResourceAllocation")
    @Operation(summary = "Add a new global resource allocation", description = "Create a new resource allocation entry")
    public ResponseEntity<String> addGlobalResourceAllocation(@RequestBody ProjectResourceMappingDTO projectResourceMappingDTO) {
        System.out.println("projectResourceMappingDTO resource allocation:"+projectResourceMappingDTO.getStartDate());
        System.out.println("projectResourceMappingDTO resource allocation:"+projectResourceMappingDTO.getAllocationPercentage());
        boolean isAdded = globalResourceAllocationService.addGlobalResourceAllocation(projectResourceMappingDTO);
        if (isAdded) {
            return ResponseEntity.ok("Resource allocation added successfully");
        } else {
            return ResponseEntity.status(400).body("Failed to add resource allocation");
        }
    }

}
