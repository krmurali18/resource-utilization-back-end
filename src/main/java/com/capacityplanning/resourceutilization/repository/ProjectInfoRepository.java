package com.capacityplanning.resourceutilization.repository;

import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectInfoRepository extends JpaRepository<ProjectInfoEntity, Long> {
    public List<ProjectInfoEntity> findAll();

    @Query("SELECT prm FROM ProjectInfoEntity prm WHERE prm.status = 'REQUESTED'")
    public List<ProjectInfoEntity> findNewProjects();
}
