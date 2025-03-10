package com.capacityplanning.resourceutilization.service.serviceImpl;

import com.capacityplanning.resourceutilization.dto.ProjectResourceMappingDTO;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import com.capacityplanning.resourceutilization.repository.ProjectResourceMappingRepository;
import com.capacityplanning.resourceutilization.service.GlobalResourceAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<ProjectResourceMappingDTO> getAvailableResourcesForDateRange(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return projectResourceMappingRepository.findByAllocationPercentageGreaterThanEqualAndStartDateLessThanEqualAndEndDateGreaterThanEqual(0.5, start, end)
            .stream()
            .map(ProjectResourceMappingDTO::new)
            .collect(Collectors.toList());
    }
}
