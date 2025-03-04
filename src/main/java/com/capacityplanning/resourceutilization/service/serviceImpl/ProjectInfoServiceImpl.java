package com.capacityplanning.resourceutilization.service.serviceImpl;

import com.capacityplanning.resourceutilization.dto.ProjectInfoDTO;
import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;
import com.capacityplanning.resourceutilization.repository.ProjectInfoRepository;
import com.capacityplanning.resourceutilization.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public ProjectInfoDTO saveProject(ProjectInfoDTO projectInfoDTO) {
        ProjectInfoEntity projectInfoEntity = new ProjectInfoEntity();
        projectInfoEntity.setGroupName(projectInfoDTO.getGroupName());
        projectInfoEntity.setTask(projectInfoDTO.getTask());
        projectInfoEntity.setSkill(projectInfoDTO.getSkill());
        projectInfoEntity.setStartDate(projectInfoDTO.getStartDate());
        projectInfoEntity.setEndDate(projectInfoDTO.getEndDate());
        projectInfoEntity.setDescription(projectInfoDTO.getDescription());
        projectInfoEntity.setCreatedAt(LocalDateTime.now());
        projectInfoEntity.setCreatedBy("Murali");
        projectInfoEntity.setUpdatedAt(LocalDateTime.now());
        projectInfoEntity.setUpdatedBy("Murali");
        return new ProjectInfoDTO(projectInfoRepository.save(projectInfoEntity));
    }
}
