package com.capacityplanning.resourceutilization.repository;

import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectResourceMappingRepository extends JpaRepository<ProjectResourceMappingEntity, Long> {
    public List<ProjectResourceMappingEntity> findAll();

}
