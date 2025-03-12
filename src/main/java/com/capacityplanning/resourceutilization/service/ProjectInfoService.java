package com.capacityplanning.resourceutilization.service;

import com.capacityplanning.resourceutilization.dto.ProjectInfoDTO;
import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;

import java.util.List;

public interface ProjectInfoService {
    public List<ProjectInfoDTO> getProjects();
    public ProjectInfoDTO saveProject(ProjectInfoDTO projectInfoDTO);
    public ProjectInfoDTO updateProject(Long id, ProjectInfoDTO projectInfoDTO);

    public List<ProjectInfoDTO> getNewProjects();
}
