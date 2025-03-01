package com.capacityplanning.resourceutilization.controller;

import com.capacityplanning.resourceutilization.dto.ProjectInfoDTO;
import com.capacityplanning.resourceutilization.service.GlobalResourceAllocationService;
import com.capacityplanning.resourceutilization.service.ProjectInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/project-info")
@Tag(name = "project_info", description = "Project Info API")
public class ProjectInfoController {
    @Autowired
    ProjectInfoService projectInfoService;

    @GetMapping("/")
    @Operation(summary = "Get all the active projects", description = "Retrieve a list of all available projects")
    public ResponseEntity<List<ProjectInfoDTO>> getProjects() {
       return ResponseEntity.ok(projectInfoService.getProjects());
    }

}
