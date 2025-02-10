package com.capacityplanning.resourceutilization.service.serviceImpl;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import com.capacityplanning.resourceutilization.repository.ProjectResourceMappingRepository;
import com.capacityplanning.resourceutilization.service.GlobalResourceAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class GlobalResourceAllocationServiceImpl implements GlobalResourceAllocationService {

    @Autowired
    private ProjectResourceMappingRepository projectResourceMappingRepository;

    @Override
    public List<ProjectResourceMappingDTO> getGlobalResourceAllocations() {
        return projectResourceMappingRepository.findAll().stream().map(ProjectResourceMappingDTO::new).collect(Collectors.toList());
    }

    @Override
    public ProjectResourceMappingDTO updateGlobalResourceAllocation(Long id, ProjectResourceMappingDTO projectResourceMappingDTO) {
        return new ProjectResourceMappingDTO(projectResourceMappingRepository.save(projectResourceMappingDTO.toEntity()));
    }
}
