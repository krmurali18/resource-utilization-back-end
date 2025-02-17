package com.capacityplanning.resourceutilization.repository;

import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectInfoRepository extends JpaRepository<ProjectInfoEntity, Long> {
    public List<ProjectInfoEntity> findAll();
}
