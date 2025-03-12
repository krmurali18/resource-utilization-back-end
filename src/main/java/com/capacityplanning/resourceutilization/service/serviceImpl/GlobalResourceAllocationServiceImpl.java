package com.capacityplanning.resourceutilization.service.serviceImpl;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import com.capacityplanning.resourceutilization.dto.ResourceAvailabilityDTO;
import com.capacityplanning.resourceutilization.dto.ResourceInfoDTO;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import com.capacityplanning.resourceutilization.entity.ResourceInfoEntity;
import com.capacityplanning.resourceutilization.repository.ProjectResourceMappingRepository;
import com.capacityplanning.resourceutilization.repository.ResourceInfoRepository;
import com.capacityplanning.resourceutilization.service.GlobalResourceAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
public class GlobalResourceAllocationServiceImpl implements GlobalResourceAllocationService {

    @Autowired
    private ProjectResourceMappingRepository projectResourceMappingRepository;

    @Autowired
    private ResourceInfoRepository resourceInfoRepository;

    @Override
    public List<ProjectResourceMappingDTO> getGlobalResourceAllocations() {
        return projectResourceMappingRepository.findAll().stream().map(ProjectResourceMappingDTO::new).collect(Collectors.toList());
    }

    @Override
    public ProjectResourceMappingEntity updateGlobalResourceAllocation(Long id, ProjectResourceMappingDTO projectResourceMappingDTO) {

        Optional<ProjectResourceMappingEntity> projectResourceMappingEntityOptional = projectResourceMappingRepository.findById(id);
        ProjectResourceMappingEntity projectResourceMappingEntity = null;
        if (projectResourceMappingEntityOptional.isPresent()) {
            projectResourceMappingEntity = projectResourceMappingEntityOptional.get();
            projectResourceMappingEntity.setAllocationPercentage(projectResourceMappingDTO.getAllocationPercentage());
            projectResourceMappingEntity.setUpdatedBy("Murali");
            projectResourceMappingEntity.setResourceId(projectResourceMappingDTO.getResourceId());
        }
        return projectResourceMappingRepository.save(projectResourceMappingEntity);
//        } else {
//            // Handle the case where the entity is not found
//            throw new Exception("Entity with id " + id + " not found");
//        }
    }

    @Override
    public boolean addGlobalResourceAllocation(ProjectResourceMappingDTO projectResourceMappingDTO) {
        return projectResourceMappingRepository.saveAndFlush(projectResourceMappingDTO.toEntity()) != null;
    }

    @Override
    public List<ResourceAvailabilityDTO> getAvailableResources(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return projectResourceMappingRepository.findAvailableResourcesForDateRange(start, end);

        // return projectResourceMappingRepository.findAvailableResourcesForDateRange(start, end)
        //     .stream()
        //     .map(resourceId -> {
        //     ResourceInfoEntity resourceInfoEntity = resourceInfoRepository.findById(resourceId).orElseThrow(() -> new RuntimeException("Resource not found"));
        //     return convertToResourceInfoDTO(resourceInfoEntity);
        //     })
        //     .collect(Collectors.toList());
    }

    private ResourceInfoDTO convertToResourceInfoDTO(ResourceInfoEntity resourceInfoEntity){

        ResourceInfoDTO resourceInfoDTO = new ResourceInfoDTO();
        resourceInfoDTO.setResourceId(resourceInfoEntity.getId());
        resourceInfoDTO.setResourceName(resourceInfoEntity.getResourceName());
        resourceInfoDTO.setSkills(resourceInfoEntity.getSkills());
        return resourceInfoDTO;
    }

    @Override
    public List<ProjectResourceMappingDTO> getNewProjects() {
        return projectResourceMappingRepository.findNewProjects().stream().map(ProjectResourceMappingDTO::new).collect(Collectors.toList());
    }

}
