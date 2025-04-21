package com.capacityplanning.resourceutilization.repository;

import com.capacityplanning.resourceutilization.entity.ProjectInfoEntity;
import com.capacityplanning.resourceutilization.entity.ProjectResourceMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjectInfoRepository extends JpaRepository<ProjectInfoEntity, Long> {
    public List<ProjectInfoEntity> findAll();

    @Query("SELECT prm FROM ProjectInfoEntity prm WHERE prm.status = 'REQUESTED'")
    public List<ProjectInfoEntity> findNewProjects();

    @Query("SELECT prm FROM ProjectInfoEntity prm WHERE prm.groupName = :groupName and prm.task = :task")
    public Optional<ProjectInfoEntity> findByGroupNameAndTask(@Param("groupName") String groupName, @Param("task") String task);
}
