package com.capacityplanning.resourceutilization.service;

import com.capacityplanning.resourceutilization.dto.ProjectInfoDTO;
import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;

import java.util.List;

public interface ProjectInfoService {
    public List<ProjectInfoDTO> getProjects();
}
