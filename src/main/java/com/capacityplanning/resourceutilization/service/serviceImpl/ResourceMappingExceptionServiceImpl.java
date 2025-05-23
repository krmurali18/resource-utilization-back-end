package com.capacityplanning.resourceutilization.service.serviceImpl;

import com.capacityplanning.resourceutilization.dto.ResourceMappingExceptionDTO;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import com.capacityplanning.resourceutilization.entity.ResourceMappingExceptionsEntity;
import com.capacityplanning.resourceutilization.repository.ProjectResourceMappingRepository;
import com.capacityplanning.resourceutilization.repository.ResourceMappingExceptionsRepository;
import com.capacityplanning.resourceutilization.service.ResourceMappingExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResourceMappingExceptionServiceImpl implements ResourceMappingExceptionService {
    @Autowired
    ResourceMappingExceptionsRepository resourceMappingExceptionsRepository;

    @Autowired
    ProjectResourceMappingRepository projectResourceMappingRepository;

    @Override
    public List<ResourceMappingExceptionDTO> getMostRecentExceptions() {
//        List<ResourceMappingExceptionsEntity> exceptions = resourceMappingExceptionsRepository.findAll().stream()
//                .filter(exception -> Arrays.asList("INSERT", "UPDATE", "ALLOCATED").contains(exception.getSource()))
//                .collect(Collectors.toList());

        List<ResourceMappingExceptionsEntity> exceptions = resourceMappingExceptionsRepository.findAll().stream()
                .collect(Collectors.toList());

        Map<String, ResourceMappingExceptionsEntity> mostRecentExceptions = new HashMap<>();

        for (ResourceMappingExceptionsEntity exception : exceptions) {
            String key = exception.getResourceId() + "-" + exception.getProjectId();
            if (!mostRecentExceptions.containsKey(key) ||
                    exception.getCreatedAt().isAfter(mostRecentExceptions.get(key).getCreatedAt())) {
                mostRecentExceptions.put(key, exception);
            }
        }

        mostRecentExceptions.entrySet().removeIf(entry -> {
            ProjectResourceMappingEntity projectResourceMappingEntity = null;
            Long mappingId = entry.getValue().getMappingId();
            Integer projectId = entry.getValue().getProjectId();
            Integer resourceId = entry.getValue().getResourceId();
            LocalDate startDate = entry.getValue().getStartDate();
            LocalDate endDate = entry.getValue().getEndDate();

            projectResourceMappingEntity = projectResourceMappingRepository.findByResourceIdAndProjectIdAndStartDateAndEndDate(
                    resourceId, projectId, startDate, endDate);


            //projectResourceMappingEntity = projectResourceMappingRepository.findByMappingId(mappingId);
            return (((projectResourceMappingEntity != null) &&
                    ((projectResourceMappingEntity.getSource().equals(entry.getValue().getSource())) ||
                    (projectResourceMappingEntity.getAllocationPercentage().equals(entry.getValue().getAllocationPercentage()))))
            );
        });


        return mostRecentExceptions.values().stream()
                .map(exception -> new ResourceMappingExceptionDTO(exception))
                .collect(Collectors.toList());
    }
}
