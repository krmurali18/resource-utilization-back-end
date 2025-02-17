package com.capacityplanning.resourceutilization.repository;

import com.capacityplanning.resourceutilization.entity.ResourceInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceInfoRepository extends JpaRepository<ResourceInfoEntity, Long> {
    public List<ResourceInfoEntity> findAll();
}
