package com.capacityplanning.resourceutilization.controller;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import com.capacityplanning.resourceutilization.service.GlobalResourceAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GlobalResourceAllocationController {


    @Autowired
    GlobalResourceAllocationService globalResourceAllocationService;

    @GetMapping("/api/global-resource-allocations")
    /*public ResponseEntity<List<ProjectResourceMappingDTO>> getGlobalResourceAllocations() {
        return ResponseEntity.ok(globalResourceAllocationService.getGlobalResourceAllocations());
    }*/
    public List<ProjectResourceMappingDTO> getGlobalResourceAllocations() {
        return globalResourceAllocationService.getGlobalResourceAllocations();
    }


}
