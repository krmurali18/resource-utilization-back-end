package com.capacityplanning.resourceutilization.service.serviceImpl;

import com.capacityplanning.resourceutilization.dto.ResourceInfoDTO;
import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;
import com.capacityplanning.resourceutilization.entity.ResourceInfoEntity;
import com.capacityplanning.resourceutilization.repository.ResourceInfoRepository;
import com.capacityplanning.resourceutilization.service.ResourceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceInfoServiceImpl implements ResourceInfoService {
    @Autowired
    ResourceInfoRepository resourceInfoRepository;

    @Override
    public List<ResourceInfoDTO> getResources() {
        return resourceInfoRepository.findAll().stream().map(ResourceInfoDTO::new).collect(Collectors.toList());
    }

    @Override
    public ResourceInfoDTO getResourceById(Long id) {
        return resourceInfoRepository.findById(id)
                .map(ResourceInfoDTO::new)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));
    }

    @Override
    public ResourceInfoDTO saveResource(ResourceInfoDTO resourceInfoDTO) {
        ResourceInfoEntity resourceInfoEntity = new ResourceInfoEntity();
        resourceInfoEntity.setResourceName(resourceInfoDTO.getResourceName());
        resourceInfoEntity.setCompany(resourceInfoDTO.getCompany());
        resourceInfoEntity.setCreatedBy("System");
        resourceInfoEntity.setCreatedAt(LocalDateTime.now());
        resourceInfoEntity.setUpdatedBy("System");
        resourceInfoEntity.setUpdatedAt(LocalDateTime.now());
        return new ResourceInfoDTO(resourceInfoRepository.save(resourceInfoEntity));
    }

    @Override
    public ResourceInfoDTO updateResourceById(Long resourceId, ResourceInfoDTO resourceInfoDTO) {
        ResourceInfoEntity resourceInfoEntity = resourceInfoRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + resourceId));
        //resourceInfoEntity.setResourceName(resourceInfoDTO.getResourceName());
        //resourceInfoEntity.setSkills(resourceInfoDTO.getSkills());
        //resourceInfoEntity.setCountry(resourceInfoDTO.getCompany());
        //resourceInfoEntity.setEmployeeType(resourceInfoDTO.getEmployeeType());
        resourceInfoEntity.setUpdatedBy("Murali");
        resourceInfoEntity.setUpdatedAt(LocalDateTime.now());
        resourceInfoEntity.setActive(resourceInfoDTO.isActive());
        return new ResourceInfoDTO(resourceInfoRepository.save(resourceInfoEntity));
    }
}
