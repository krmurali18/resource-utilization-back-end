package com.capacityplanning.resourceutilization.controller;

import com.capacityplanning.resourceutilization.dto.ResourceInfoDTO;
import com.capacityplanning.resourceutilization.service.ResourceInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resource-info")
@Tag(name = "resource_info", description = "Resource Info API")
public class ResourceInfoController {
    @Autowired
    ResourceInfoService resourceInfoService;

    @GetMapping("/")
    @Operation(summary = "Get all the active Resources", description = "Retrieve a list of all resources")
     public List<ResourceInfoDTO> getResources() {
       return resourceInfoService.getResources();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Resource by ID", description = "Retrieve resource information by its ID")
    public ResourceInfoDTO getResourceById(@PathVariable Long id) {
      return resourceInfoService.getResourceById(id);
    }

}
