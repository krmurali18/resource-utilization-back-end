package com.capacityplanning.resourceutilization.controller;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import com.capacityplanning.resourceutilization.service.GlobalResourceAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/global-resource-allocations")
@Tag(name = "global_resource_allocation", description = "Global Resource Allocation API")
public class GlobalResourceAllocationController {


    @Autowired
    GlobalResourceAllocationService globalResourceAllocationService;

    @GetMapping("/")
    @Operation(summary = "Get all global resource allocation", description = "Retrieve a list of all resource allocations")
    public ResponseEntity<List<ProjectResourceMappingDTO>> getGlobalResourceAllocations() {
        return ResponseEntity.ok(globalResourceAllocationService.getGlobalResourceAllocations());
    }


    @PutMapping("/api/global-resource-allocations/{id}")
    public ResponseEntity<ProjectResourceMappingDTO> updateGlobalResourceAllocation(@PathVariable Long id, @RequestBody ProjectResourceMappingDTO projectResourceMappingDTO) {
        return ResponseEntity.ok(globalResourceAllocationService.updateGlobalResourceAllocation(id, projectResourceMappingDTO));
    }

    // public List<ProjectResourceMappingDTO> getGlobalResourceAllocations() {
    //   return globalResourceAllocationService.getGlobalResourceAllocations();
    //}


}
