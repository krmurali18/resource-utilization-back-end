package com.capacityplanning.resourceutilization.service;

import com.capacityplanning.resourceutilization.dto.ResourceMappingExceptionDTO;

import java.util.List;

public interface ResourceMappingExceptionService {
    public List<ResourceMappingExceptionDTO> getMostRecentExceptions();
}
