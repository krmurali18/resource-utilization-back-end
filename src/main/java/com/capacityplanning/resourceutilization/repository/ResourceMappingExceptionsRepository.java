package com.capacityplanning.resourceutilization.repository;

import com.capacityplanning.resourceutilization.entity.ResourceMappingExceptionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceMappingExceptionsRepository extends JpaRepository<ResourceMappingExceptionsEntity, Long> {
    public List<ResourceMappingExceptionsEntity> findAll();
}
