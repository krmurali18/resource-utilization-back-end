package com.capacityplanning.resourceutilization.service.serviceImpl;

import com.capacityplanning.resourceutilization.dto.ProjectInfoDTO;
import com.capacityplanning.resourceutilization.repository.ProjectInfoRepository;
import com.capacityplanning.resourceutilization.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {
    @Autowired
    ProjectInfoRepository projectInfoRepository;

    @Override
    public List<ProjectInfoDTO> getProjects() {
        return projectInfoRepository.findAll().stream().map(ProjectInfoDTO::new).collect(Collectors.toList());
    }
}
