package com.capacityplanning.resourceutilization.service;

import com.capacityplanning.resourceutilization.dto.ProjectInfoDTO;
import com.capacityplanning.resourceutilization.dto.ResourceInfoDTO;

import java.util.List;

public interface ResourceInfoService {
    public List<ResourceInfoDTO> getResources();
    public ResourceInfoDTO getResourceById(Long resourceId);
    public ResourceInfoDTO saveResource(ResourceInfoDTO resourceInfoDTO);

    public ResourceInfoDTO updateResourceById(Long resourceId, ResourceInfoDTO resourceInfoDTO);
}

