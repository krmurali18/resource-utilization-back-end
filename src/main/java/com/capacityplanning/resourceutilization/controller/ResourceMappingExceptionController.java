package com.capacityplanning.resourceutilization.controller;

import com.capacityplanning.resourceutilization.dto.ResourceMappingExceptionDTO;
import com.capacityplanning.resourceutilization.service.ResourceMappingExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/resource-mapping-exception")
@Tag(name = "resource-mapping-exception", description = "Resource Mapping Exceptions API")
public class ResourceMappingExceptionController {
    @Autowired
    private ResourceMappingExceptionService resourceMappingExceptionService;

    @GetMapping("/get-latest-exceptions")
    @Operation(summary = "Get most recent exceptions for each resource and project")
    public ResponseEntity<List<ResourceMappingExceptionDTO>> getMostRecentExceptions() {
        List<ResourceMappingExceptionDTO> latestExceptions = resourceMappingExceptionService.getMostRecentExceptions();
        return ResponseEntity.ok(latestExceptions);
    }

}