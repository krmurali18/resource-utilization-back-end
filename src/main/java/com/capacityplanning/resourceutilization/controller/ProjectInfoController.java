package com.capacityplanning.resourceutilization.controller;

import com.capacityplanning.resourceutilization.dto.ProjectInfoDTO;
import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import com.capacityplanning.resourceutilization.service.GlobalResourceAllocationService;
import com.capacityplanning.resourceutilization.service.ProjectInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @PostMapping("/save-project")
    @Operation(summary = "Save project info", description = "Save project information into the database")
    public ResponseEntity<ProjectInfoDTO> saveProject(@RequestBody ProjectInfoDTO projectInfoDTO) {
        ProjectInfoDTO savedProject = projectInfoService.saveProject(projectInfoDTO);
        return ResponseEntity.ok(savedProject);
    }

    @PostMapping("/update-project/{id}")
    @Operation(summary = "Update project info", description = "Update project information for a given ID")
    public ResponseEntity<ProjectInfoDTO> updateProject(@PathVariable Long id, @RequestBody ProjectInfoDTO projectInfoDTO) {
        ProjectInfoDTO updatedProject = projectInfoService.updateProject(id, projectInfoDTO);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping("/get-new-projects")
    @Operation(summary = "Get all new projects", description = "Retrieve a list of all projects with the type as Requested")
    public ResponseEntity<List<ProjectInfoDTO>> getNewProjects() {
        List<ProjectInfoDTO> requestedProjects = projectInfoService.getNewProjects();
        return ResponseEntity.ok(requestedProjects);
    }

}
