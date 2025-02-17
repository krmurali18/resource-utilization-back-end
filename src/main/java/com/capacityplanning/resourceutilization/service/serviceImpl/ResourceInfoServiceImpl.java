package com.capacityplanning.resourceutilization.service.serviceImpl;

import com.capacityplanning.resourceutilization.dto.ResourceInfoDTO;
import com.capacityplanning.resourceutilization.repository.ResourceInfoRepository;
import com.capacityplanning.resourceutilization.service.ResourceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
